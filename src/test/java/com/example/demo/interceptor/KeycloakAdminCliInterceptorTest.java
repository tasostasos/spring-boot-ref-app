package com.example.demo.interceptor;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeycloakAdminCliInterceptorTest {
    @InjectMocks
    KeycloakAdminCliInterceptor keycloakAdminCliInterceptor;

    @Mock
    RestTemplate restTemplate;

    @Mock
    HttpRequest request;

    @Mock
    ClientHttpRequestExecution execution;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(keycloakAdminCliInterceptor, "tokenUrl", "tokenUrl");
        ReflectionTestUtils.setField(keycloakAdminCliInterceptor, "clientId", "clientId");
        ReflectionTestUtils.setField(keycloakAdminCliInterceptor, "username", "username");
        ReflectionTestUtils.setField(keycloakAdminCliInterceptor, "password", "password");
    }

    @Test
    void intercept() throws IOException {
        ClientHttpResponse mockResponse = mock(ClientHttpResponse.class);
        when(execution.execute(any(HttpRequest.class), any(byte[].class))).thenReturn(mockResponse);// Create a mock ResponseEntity object
        ResponseEntity<Map> mockResponseEntity = mock(ResponseEntity.class);
        Map mockMap = mock(Map.class);
        when(mockResponseEntity.getBody()).thenReturn(mockMap);
        when(mockMap.get("access_token")).thenReturn("mockToken");
        when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), eq(Map.class))).thenReturn(mockResponseEntity);
        HttpHeaders mockHeaders = mock(HttpHeaders.class);
        when(request.getHeaders()).thenReturn(mockHeaders);
        ClientHttpResponse response = keycloakAdminCliInterceptor.intercept(request, "".getBytes(), execution);

        verify(restTemplate, times(1)).exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class));
        verify(mockResponseEntity, times(1)).getBody();
        verify(mockMap, times(1)).get("access_token");
        verify(mockHeaders, times(1)).add("Authorization", "Bearer mockToken");
        assertEquals(mockResponse, response);
    }
}