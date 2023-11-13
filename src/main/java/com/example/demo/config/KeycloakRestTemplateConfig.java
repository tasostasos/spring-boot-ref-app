package com.example.demo.config;

import com.example.demo.interceptor.KeycloakAdminCliInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Rest template that authenticates via access token for calling keycloak REST api. {@link KeycloakAdminCliInterceptor}
 * fetches access token for admin user, and sets it in request authorization header as Bearer token.
 */
@Configuration
public class KeycloakRestTemplateConfig {
    @Autowired
    KeycloakAdminCliInterceptor keycloakInterceptor;

    /**
     * {@link RestTemplate} for keycloak REST API calls .Uses {@link KeycloakAdminCliInterceptor} for authenticating with admin
     * roles .
     * @return
     */
    @Bean
    public RestTemplate keycloakRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(keycloakInterceptor);
        return restTemplate;
    }
}
