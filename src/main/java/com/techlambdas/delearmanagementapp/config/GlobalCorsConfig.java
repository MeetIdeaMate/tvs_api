package com.techlambdas.delearmanagementapp.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "PUT", "POST", "PATCH", "DELETE", "OPTIONS", "HEAD")
                        .allowedHeaders("Origin","Content-Type","X-Amz-Date","Authorization","X-Api-Key","X-Amz-Security-Token","locale","Access-Control-Allow-Origin", "Access-Control-Allow-Headers","Access-Control-Allow-Methods")
                        .maxAge(3600);
            }
        };
    }
}
