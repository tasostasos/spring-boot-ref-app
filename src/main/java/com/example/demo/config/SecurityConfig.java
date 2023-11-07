package com.example.demo.config;

import com.example.demo.security.CustomJwtAuthenticationConverter;
import com.example.demo.security.KeycloakLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final KeycloakLogoutHandler keycloakLogoutHandler;

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/"))
                .permitAll();

        http.authorizeRequests()
                .requestMatchers(new AntPathRequestMatcher("/customers*"))
                .hasAnyAuthority("ROLE_USER", "OIDC_USER")
                .anyRequest()
                .authenticated();

        http.oauth2Login(Customizer.withDefaults())
                .logout((oath) -> oath.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));


        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtt -> jwtt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())));
        return http.build();
    }

}
