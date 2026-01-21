package com.lanpodder.rest_sample.configuration.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
public class WebSecurityConfig {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http){
    return http
      .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        .requestMatchers("/openapi/**").permitAll()
        .anyRequest().authenticated())
      .sessionManagement(sessionManagement -> sessionManagement
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .cors(cors -> cors.configurationSource(request -> {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(false);
        return corsConfiguration;
      }))
      .csrf(AbstractHttpConfigurer::disable)
      .oauth2ResourceServer(oauth2ResourceSerer -> oauth2ResourceSerer
        .jwt(jwt -> {}))
      .formLogin(AbstractHttpConfigurer::disable)
      .build();
  }
}
