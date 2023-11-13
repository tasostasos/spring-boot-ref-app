package com.example.demo.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RestTemplateConfigTest {
    @InjectMocks
    private RestTemplateConfig restTemplateConfig;

    @Test
    void keycloakRestTemplate() {

        RestTemplate result = restTemplateConfig.restTemplate();

        assertNotNull(result);
    }
}