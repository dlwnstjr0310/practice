package com.study.apigateway.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.token.ExpiredTokenException;
import com.study.apigateway.auth.exception.token.InvalidTokenException;
import com.study.apigateway.auth.exception.token.JsonParsingException;
import com.study.apigateway.auth.jwt.TokenParser;
import com.study.apigateway.auth.model.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class GatewayHeaderFilter extends AbstractGatewayFilterFactory<GatewayHeaderFilter.Config> {

	private final TokenParser tokenParser;

	public GatewayHeaderFilter(TokenParser tokenParser) {
		super(Config.class);
		this.tokenParser = tokenParser;
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {

			String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

			try {
				/* todo: 토큰이 비어있을때 설정 추가해야함
				 * 토큰이 유효하면 다음 필터로 이동
				 * 토큰이 없거나 만료된경우 path 가 /auth/** 면 ㄱㅊ
				 * 근데 나 이거 설정 안한것같은데 왜 되지;
				 * */
				String path = exchange.getRequest().getPath().value();

				if (path.startsWith("/auth")) {
					return chain.filter(exchange);
				}

				if (!ObjectUtils.isEmpty(token) && tokenParser.isValidToken(token)) {
					return chain.filter(exchange);
				}

			} catch (ExpiredTokenException e) {
				return createErrorResponse(exchange.getResponse(), Error.EXPIRED_TOKEN, HttpStatus.SC_FORBIDDEN);
			} catch (InvalidTokenException e) {
				return createErrorResponse(exchange.getResponse(), Error.INVALID_TOKEN, HttpStatus.SC_FORBIDDEN);
			}

			return chain.filter(exchange);
		};
	}

	private Mono<Void> createErrorResponse(ServerHttpResponse response, Error error, int code) {

		response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		response.getHeaders().add(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

		Response<Void> customResponse = Response.<Void>builder()
				.code(error.getCode())
				.message(error.getMessage())
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

	public static class Config {
	}
}
