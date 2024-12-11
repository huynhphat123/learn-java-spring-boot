package com.phatdev.configuration;

import com.phatdev.entity.User;
import com.phatdev.enums.Role;
import com.phatdev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor // Tự động tạo constructor cho các trường final (userRepository, userMapper)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Đặt tất cả các trường là private và final (không thay đổi được sau khi khởi tạo)
@Slf4j

public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
             if (userRepository.findByUsername("admin").isEmpty()) {
                 var roles = new HashSet<String>();
                 roles.add(Role.ADMIN.name());
                 User user = User.builder()
                         .username("admin")
                         .password(passwordEncoder.encode("admin"))
                         .roles(roles)
                         .build();

                 userRepository.save(user);
                 log.warn("Admin user has been created with default password: admin,  please change it!");
            }
        };
    }
}
