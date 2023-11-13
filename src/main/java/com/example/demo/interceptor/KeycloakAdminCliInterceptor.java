package com.example.demo.interceptor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * Interceptor for fetching admin0-cli access token for api calls to keycloak REST API.Adds token to
 * {@link RestTemplate} requests as Authorization header(Bearer token).
 */
@Component
public class KeycloakAdminCliInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    RestTemplate restTemplate;
    @Value("${app.keycloak.open-id-connect}")
    private String tokenUrl;

    @Value("${app.keycloak.open-id-connect-client-id}")
    private String clientId;

    @Value("${app.keycloak.open-id-connect-username}")
    private String username;

    @Value("${app.keycloak.open-id-connect-password}")
    private String password;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // Create a map with the request parameters for the password grant
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "password");
        map.add("client_id", clientId);
        map.add("username", username);
        map.add("password", password);

        // Create an HttpEntity object with the headers and parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<> (map, headers);

        // Send the request to the Keycloak token endpoint and get the response
        ResponseEntity<Map> responseEntity = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, Map.class);

        // Extract the access token from the response body
        String accessToken = responseEntity.getBody().get("access_token").toString();

        // Add the access token to the request header
        request.getHeaders().add("Authorization", "Bearer " + accessToken);

        // Execute the request and return the response
        return execution.execute(request, body);
    }
}
