package com.study.apigateway.auth.exception.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.token.JsonParsingException;
import com.study.apigateway.auth.model.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class AccessDeniedExceptionHandler implements ServerAccessDeniedHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {

		ServerHttpResponse response = exchange.getResponse();

		response.setStatusCode(HttpStatus.FORBIDDEN);
		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		Response<Void> customResponse = Response.<Void>builder()
				.code(Error.PERMISSION_DENIED.getCode())
				.message(Error.PERMISSION_DENIED.getMessage())
				.build();

		String json;

		try {
			json = new ObjectMapper().writeValueAsString(customResponse);
		} catch (JsonProcessingException e) {
			throw new JsonParsingException();
		}

		byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
		DataBuffer buffer = response.bufferFactory().wrap(bytes);

		return response.writeWith(Mono.just(buffer));
	}
}
