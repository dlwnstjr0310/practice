package com.study.apigateway.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

		http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.cors(corsSpec -> corsSpec.configurationSource(request -> {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setMaxAge(3600L);

					return config;
				}));

		return http.build();
	}
}