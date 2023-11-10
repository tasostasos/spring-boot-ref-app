package com.example.demo.config;

import com.example.demo.interceptor.KeycloakInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KeycloakRestTemplateConfig {
    @Autowired
    KeycloakInterceptor keycloakInterceptor;

    @Bean
    public RestTemplate keycloakRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(keycloakInterceptor);
        return restTemplate;
    }
}
