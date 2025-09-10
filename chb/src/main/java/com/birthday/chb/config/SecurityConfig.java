package com.birthday.chb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화합니다.
            // stateless API(세션이 없는)에서는 보통 비활성화합니다.
            .csrf(csrf -> csrf.disable())

            // 세션을 사용하지 않도록 설정합니다. (stateless)
            // REST API 서버에 적합한 설정입니다.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // HTTP 요청에 대한 접근 권한을 설정합니다.
            .authorizeHttpRequests(authz -> authz
                // "/api/**" 패턴의 URL에 대한 요청은 인증 없이 모두 허용합니다.
                .requestMatchers("/api/**").permitAll()
                // 그 외의 모든 요청도 일단은 모두 허용합니다. (필요에 따라 .authenticated() 등으로 변경 가능)
                .anyRequest().permitAll()
            );

        return http.build();
    }
}