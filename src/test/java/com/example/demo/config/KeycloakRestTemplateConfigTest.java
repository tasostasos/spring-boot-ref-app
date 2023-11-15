package com.example.demo.config;

import com.example.demo.interceptor.KeycloakInterceptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class KeycloakRestTemplateConfigTest {

    @Mock
    private KeycloakInterceptor keycloakAdminCliInterceptor;

    @InjectMocks
    private KeycloakRestTemplateConfig keycloakRestTemplateConfig;

    @Test
    void keycloakRestTemplate() {

        RestTemplate result = keycloakRestTemplateConfig.keycloakRestTemplate();

        assertNotNull(result);
        assertEquals(1, result.getInterceptors().size());

    }
}