package com.travel.travelling.config;

import com.travel.travelling.entity.Role;
import com.travel.travelling.entity.User;
import com.travel.travelling.constant.RolePrefix;
import com.travel.travelling.repository.RoleRepository;
import com.travel.travelling.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class ApplicationRunnerInit implements ApplicationRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String ADMIN_EMAIL = "admin@gmail.com";
    private final String ADMIN_PASSWORD = "admin";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.findByEmail(ADMIN_EMAIL).isEmpty()){
            Role userRole = roleRepository.findById(RolePrefix.USER)
                    .orElseGet(() -> roleRepository.save(new Role(RolePrefix.USER, "User Role")));

            Role admin = roleRepository.findById(RolePrefix.ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(RolePrefix.ADMIN, "Admin Role")));

            HashSet<Role> roles = new HashSet<>();
            roles.add(admin);

            userRepository.save(User.builder()
                            .email(ADMIN_EMAIL)
                            .password(passwordEncoder.encode(ADMIN_PASSWORD))
                            .roles(roles)
                    .build());
        }
    }
}
