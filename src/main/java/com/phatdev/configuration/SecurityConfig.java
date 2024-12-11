package com.phatdev.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Danh sách các endpoint (điểm cuối) công khai, không yêu cầu xác thực
    private final String[] PUBLIC_ENDPOINTS = {"/users", "auth/token", "auth/introspect"};

    // Cấu hình bảo mật của ứng dụng
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Cấu hình quyền truy cập
        http.authorizeHttpRequests(requests ->
                // Cho phép truy cập tự do đến các endpoint trong PUBLIC_ENDPOINTS với phương thức POST
                requests.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
                        // Các request khác yêu cầu phải xác thực
                        .anyRequest().authenticated());

        // Cấu hình ứng dụng như một OAuth2 Resource Server với JWT
        http.oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder())));

        // Vô hiệu hóa bảo vệ CSRF (thích hợp cho API không dựa trên session)
        http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        // Trả về cấu hình SecurityFilterChain
        return http.build();
    }

    // Khóa bí mật dùng để ký và xác thực JWT
    @Value("${jwt.SINGER_KEY}")
    private String singerKey;

    // Cấu hình JwtDecoder để giải mã và xác thực JWT
    @Bean
    JwtDecoder jwtDecoder() {
        // Tạo khóa bí mật từ singerKey với thuật toán HMAC SHA-512 (HS512)
        SecretKeySpec secretKeySpec = new SecretKeySpec(singerKey.getBytes(), "HS512");
        // Trả về JwtDecoder với cấu hình thuật toán HS512
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
