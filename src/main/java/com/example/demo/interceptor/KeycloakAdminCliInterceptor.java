package com.example.demo.interceptor;


import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Interceptor for forwarding access token for api calls to keycloak REST API.Adds token to
 * {@link RestTemplate} requests as Authorization header(Bearer token).
 */
@Component
public class KeycloakAdminCliInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessToken = jwt.getTokenValue();
        request.getHeaders().add("Authorization", "Bearer " + accessToken);

        return execution.execute(request, body);
    }
}
