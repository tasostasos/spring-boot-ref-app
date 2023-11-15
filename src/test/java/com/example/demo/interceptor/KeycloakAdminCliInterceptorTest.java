package com.example.demo.interceptor;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeycloakAdminCliInterceptorTest {
    @InjectMocks
    KeycloakInterceptor keycloakAdminCliInterceptor;

    @Mock
    SecurityContext context;

    @Mock
    Authentication authentication;

    @Mock
    HttpHeaders httpHeaders;

    @Mock
    HttpRequest request;

    @Mock
    ClientHttpRequestExecution execution;

    @Mock
    ClientHttpResponse response;

    @Mock
    Jwt jwt;

    @Test
    void intercept() throws IOException {
        // Arrange
        KeycloakInterceptor interceptor = new KeycloakInterceptor();
        when((jwt).getTokenValue()).thenReturn("token");
        try (MockedStatic<SecurityContextHolder> sHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
            sHolder.when(() -> SecurityContextHolder.getContext()).thenReturn(context);
            when(context.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(jwt);
            when(execution.execute(any(), any())).thenReturn(response);
            when(request.getHeaders()).thenReturn(httpHeaders);
            ClientHttpResponse actual = interceptor.intercept(request, new byte[0], execution);
            assertEquals(response, actual);
        }
        verify(request).getHeaders();
        verify(request.getHeaders()).add(eq("Authorization"), eq("Bearer token"));
        verify(execution).execute(any(), any());
    }
}