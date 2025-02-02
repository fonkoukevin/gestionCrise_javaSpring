package com.example.simulateur.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // ✅ Applique CORS à toutes les routes
                        .allowedOrigins("http://localhost:4200") // ✅ Autorise Angular
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ Méthodes HTTP acceptées
                        .allowedHeaders("*") // ✅ Tous les headers sont acceptés
                        .allowCredentials(true); // ✅ Autorise l'envoi de cookies (si nécessaire)
            }
        };
    }
}
