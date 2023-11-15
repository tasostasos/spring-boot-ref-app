package com.example.demo.rest;

import com.example.demo.dto.keycloak.rest.response.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * controller for forwarding authorized api calls to keycloak REST API.
 */
@RestController
@RequestMapping("keycloak")
@RequiredArgsConstructor
public class KeycloakRestApiController {

    private final RestTemplate keycloakRestTemplate;

    @Value("${app.keycloak.rest-endpoint}")
    private String keycloakRestUrl;

    @GetMapping("/{endpoint}")
    public ResponseEntity<User[]> getUserInfo(@PathVariable String endpoint) {
        ResponseEntity<User[]> response = keycloakRestTemplate.getForEntity(keycloakRestUrl + endpoint, User[].class);
        return response;
    }

}
