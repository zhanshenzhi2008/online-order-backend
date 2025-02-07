package com.orjrs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // 关闭csrf
            .csrf(AbstractHttpConfigurer::disable)
            // 不通过Session获取SecurityContext
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置权限
            .authorizeHttpRequests(auth -> auth
                // 对于登录接口 允许匿名访问
                .requestMatchers("/api/auth/login").anonymous()
                // 允许所有人访问knife4j和actuator
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/doc.html",
                    "/webjars/**",
                    "/actuator/**"
                ).permitAll()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
            )
            .build();
    }
} 