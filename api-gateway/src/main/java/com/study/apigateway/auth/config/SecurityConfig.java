package com.study.apigateway.auth.config;

import com.study.apigateway.auth.exception.handler.AccessDeniedExceptionHandler;
import com.study.apigateway.auth.exception.handler.GatewayAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
	private final GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint;

	@Bean
	public SecurityWebFilterChain filterChain(ServerHttpSecurity http) {

		http.csrf(ServerHttpSecurity.CsrfSpec::disable);
		http.cors(ServerHttpSecurity.CorsSpec::disable);

		http.formLogin(ServerHttpSecurity.FormLoginSpec::disable);

		http.exceptionHandling(exception ->
				exception.accessDeniedHandler(accessDeniedExceptionHandler));

		http.exceptionHandling(exception ->
				exception.authenticationEntryPoint(gatewayAuthenticationEntryPoint));

		http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());

		http.authorizeExchange(request ->
				request
						.pathMatchers("/auth/logout", "/auth/token").authenticated()
						.pathMatchers("/auth/**").permitAll()
						.anyExchange().permitAll()
		);

		return http.build();
	}
}