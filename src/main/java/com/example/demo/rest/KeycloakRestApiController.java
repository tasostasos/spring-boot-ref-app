package com.example.demo.rest;

import com.example.demo.dto.keycloak.rest.response.users.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
@Tag(name = "keycloak")
@RequiredArgsConstructor
public class KeycloakRestApiController {

    private final RestTemplate keycloakRestTemplate;

    @Value("${app.keycloak.rest-endpoint}")
    private String keycloakRestUrl;

    @Operation(summary = "This method is used to get the user info.")
    @GetMapping(value = "/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getUserInfo(@PathVariable String userId) {
        ResponseEntity<Response> response = keycloakRestTemplate.getForEntity(keycloakRestUrl + "users/"
                + userId, Response.class);
        return response;
    }

}
