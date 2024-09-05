package com.study.apigateway.auth.exception.handler;

import com.study.apigateway.auth.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.study.apigateway.auth.exception.handler.FilterExceptionHandler.createErrorResponse;

@Component
@RequiredArgsConstructor
public class GatewayAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {

		ServerHttpResponse response = exchange.getResponse();

		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		return createErrorResponse(exchange.getResponse(), Error.UNAUTHORIZED);
	}

}
