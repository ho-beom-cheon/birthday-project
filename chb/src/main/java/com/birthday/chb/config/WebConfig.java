package com.birthday.chb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // API 경로에 대해 CORS 설정 적용
                .allowedOrigins(
                    "http://localhost:5173"
                             , "http://146.56.117.83:8082"
                             , "http://www.beomsnet.world"
                             , "http://beomsnet.world"
                ) // 개발 및 운영 환경 주소 허용
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}