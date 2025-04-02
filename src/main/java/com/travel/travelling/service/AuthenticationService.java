package com.travel.travelling.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.travel.travelling.dto.request.AuthenticateRequest;
import com.travel.travelling.dto.request.IntrospectRequest;
import com.travel.travelling.dto.request.InvalidateTokenRequest;
import com.travel.travelling.dto.request.RefreshTokenRequest;
import com.travel.travelling.dto.response.AuthenticateResponse;
import com.travel.travelling.dto.response.IntrospectResponse;
import com.travel.travelling.entity.InvalidateToken;
import com.travel.travelling.entity.User;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.repository.InvalidateTokenRepository;
import com.travel.travelling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final InvalidateTokenRepository invalidateTokenRepository;


    @Value("${jwt.signerKey}")
    public String signerKey;

    @Value("${jwt.valid-duration}")
    public String VALID_DURATION;

    @Value("${jwt.refresh-duration}")
    public String REFRESH_DURATION;


    // introspect (kiểm tra token)
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        String token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }


    // verify Token (kiểm tra chữ kí, Date, valid của token)
    public SignedJWT verifyToken(String token, boolean isRefreshToken) throws ParseException, JOSEException {
        // parser token (phần tích token, sử dụng SignedJWT thay vì JWSObject
        // vì SignedJWT có thể lấy được claim và nó chuyên xử lí jwt)
        SignedJWT signedJWT = SignedJWT.parse(token);

        // lấy signerKey để verify
        JWSVerifier verifier = new MACVerifier(signerKey);

        // verify
        boolean isValid = signedJWT.verify(verifier);

        if (!isValid) throw new AppException(ErrorCode.INVALID_TOKEN);
        // trả về true nếu nằm sau exp nằm sau thời điểm hiện tại
        boolean isNotExpired = isRefreshToken?
                        signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                                .plus(Long.parseLong(REFRESH_DURATION), ChronoUnit.SECONDS)
                                .isAfter(Instant.now())
                        :
        signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());

        if (isRefreshToken && !isNotExpired) throw new AppException(ErrorCode.TOKEN_IS_EXPIRED_REFRESH);


        //throw nếu không( token valid and expiryTime còn hạn )
        if(!(isValid && isNotExpired)) throw new AppException(ErrorCode.UNAUTHENTICATED);

        String UUID = signedJWT.getJWTClaimsSet().getJWTID();

        if(invalidateTokenRepository.findById(UUID).isPresent())
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }


    // authenticate (xác thực)
    public AuthenticateResponse authenticate(AuthenticateRequest request) throws JOSEException {
        // check user not found
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // check password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean isValidPass = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!isValidPass) throw new AppException(ErrorCode.PASSWORD_INCORRECT);


        // generate token
        String token = generateToken(user);

        return AuthenticateResponse.builder()
                .token(token)
                .build();
    }


    // generateToken (tạo token)
    public String generateToken(User user) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer("travel.com")
                .subject(user.getEmail())
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(Long.parseLong(VALID_DURATION), ChronoUnit.SECONDS)))
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        JWSSigner jwsSigner = new MACSigner(signerKey);

        jwsObject.sign(jwsSigner);

        return jwsObject.serialize();
    }


    public String buildScope(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            user.getRoles().forEach(role -> scopeJoiner.add("ROLE_" + role.getRoleName()));
        }
        return scopeJoiner.toString();
    }


    // save token UUID into InvalidateToken Entity when logout application
    public void logout(InvalidateTokenRequest request) throws ParseException, JOSEException {
        try{
            SignedJWT signedJWT = verifyToken(request.getToken(), true);

            String UUID = signedJWT.getJWTClaimsSet().getJWTID();

            LocalDateTime exp =signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                    .plus(Long.parseLong(REFRESH_DURATION), ChronoUnit.SECONDS)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            InvalidateToken invalidateToken = InvalidateToken.builder()
                    .UUID(UUID)
                    .expiryTime(exp)
                    .build();

            invalidateTokenRepository.save(invalidateToken);
        }
        catch (AppException e) {
            throw new AppException(ErrorCode.USER_ALREADY_LOGOUT);
        }
    }


    // refresh Token when hết hạn đăng nhập và còn thời hạn refresh Token
    public AuthenticateResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {
        // kiểm tra tính hợp lệ của token
        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        String UUID = signedJWT.getJWTClaimsSet().getJWTID();

        LocalDateTime exp =signedJWT.getJWTClaimsSet().getIssueTime().toInstant()
                .plus(Long.parseLong(REFRESH_DURATION), ChronoUnit.SECONDS)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        InvalidateToken invalidateToken = InvalidateToken.builder()
                .UUID(UUID)
                .expiryTime(exp)
                .build();

        invalidateTokenRepository.save(invalidateToken);

        User user = userRepository.findByEmail(signedJWT.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String token = generateToken(user);

        return AuthenticateResponse.builder()
                .token(token)
                .build();
    }
}
