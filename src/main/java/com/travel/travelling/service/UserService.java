package com.travel.travelling.service;

import com.travel.travelling.dto.request.UserCreationRequest;
import com.travel.travelling.dto.request.UserUpdateRequest;
import com.travel.travelling.dto.response.UserResponse;
import com.travel.travelling.entity.Role;
import com.travel.travelling.entity.User;
import com.travel.travelling.exception.AppException;
import com.travel.travelling.exception.ErrorCode;
import com.travel.travelling.mapper.UserMapper;
import com.travel.travelling.constant.RolePrefix;
import com.travel.travelling.repository.RoleRepository;
import com.travel.travelling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // create user
    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setAccountBalance(0);

        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // add roles for user
        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(RolePrefix.USER).ifPresent(roles::add);
        user.setRoles(roles);

        // save and return user
        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
    }


    // get All User
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    // update User
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse updateUser(UserUpdateRequest request) {
        User user = userRepository.findByEmail(getMyInfo().getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getPhone() != null) user.setPhone(request.getPhone());
        if (request.getCccd() != null) user.setCccd(request.getCccd());
        if (request.getBirthday() != null) user.setBirthday(request.getBirthday());

        // Chỉ cập nhật password nếu có giá trị hợp lệ
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user = userRepository.save(user);
        return userMapper.toUserResponse(user);
    }



    // delete User
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) {
        if(!userRepository.existsById(id)) throw new AppException(ErrorCode.USER_NOT_EXISTED);
        userRepository.deleteById(id);
    }


    // get my info
    @PostAuthorize("returnObject.email == authentication.name")
    public UserResponse getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        return userMapper.toUserResponse(user);
    }


    // nộp tiền vào tài khoản
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public void depositMoney(double amount) {
        String email = getMyInfo().getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        user.setAccountBalance(user.getAccountBalance() + amount);
        userRepository.save(user);
    }
}
