package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(KeycloakRestApiController.class)
class KeycloakRestApiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RestTemplate keycloakRestTemplate;
    @Value("${app.keycloak.rest-endpoint}")
    private String keycloakRestUrl;
    @Test
    public void getUserInfo() throws Exception {

        // perform a GET request to the customers page with the mock principal
        mockMvc.perform(get("/keycloak/users/1e106f1c-f65e-48da-90a7-da6d8d0a4689").with(jwt().authorities(List.of(
                                new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, "admin"))))
                .andExpect(status().isOk());
    }
}