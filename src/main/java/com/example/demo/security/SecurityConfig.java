package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Security config for authenticating /customers/* endpoint with ROLE_USER(user realm role) and OIDC_USER(authentication
 * via login page for user role) .
 * Permits unauthenticated root url,swagger endpoints.
 * authenticating /keycloak/* endpoint with ROLE_ADMIN
 * Requires authentication for all other requests via oauth2-keycloak .
 * Enables keycloak login for authentication.
 * Adds oAuth2 filter for jwt Bearer token authentication.
 */
@Configuration
@EnableWebSecurity//(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final KeycloakLogoutHandler keycloakLogoutHandler;

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {

        //authenticate via oauth2-keycloak all requests
        http.authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/"),
                        new AntPathRequestMatcher("/v3/api-docs/**"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/customers**"))
                .hasAnyAuthority("ROLE_USER", "OIDC_USER")
                .requestMatchers(new AntPathRequestMatcher("/keycloak/**"))
                .hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();


        //enable keycloak login for authentication
        http.oauth2Login(Customizer.withDefaults())
                .logout((oath) -> oath.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));

        //add oAuth2 filter for jwt Bearer token authentication
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtt -> jwtt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())));

        return http.build();
    }

}
