package com.study.apigateway.auth.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.token.DifferentTokenVersionException;
import com.study.apigateway.auth.exception.token.ExpiredTokenException;
import com.study.apigateway.auth.exception.token.InvalidTokenException;
import com.study.apigateway.auth.exception.token.JsonParsingException;
import com.study.apigateway.auth.model.response.Response;
import com.study.apigateway.auth.util.TokenParser;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

@Component
public class GatewayHeaderFilter extends AbstractGatewayFilterFactory<GatewayHeaderFilter.Config> {

	private static final String[] EXCLUDED_PATHS = {"/", "/auth/join", "/auth/mail-certification/send", "/auth/mail-certification/verify", "/auth/login"};

	private final TokenParser tokenParser;

	public GatewayHeaderFilter(TokenParser tokenParser) {
		super(Config.class);
		this.tokenParser = tokenParser;
	}

	@Override
	public GatewayFilter apply(Config config) {

		return (exchange, chain) -> {

			String path = exchange.getRequest().getPath().value();

			for (String excludedPath : EXCLUDED_PATHS) {
				if (path.equals(excludedPath)) {
					return chain.filter(exchange);
				}
			}

			try {

				String token = tokenParser.getJwt(exchange.getRequest());
				String deviceId = generateDeviceId(exchange.getRequest());

				if (!ObjectUtils.isEmpty(token)) {

					// todo: 이걸로 권한별 라우팅 설정하기. 좀만있다가..
					SimpleGrantedAuthority role = tokenParser.isValidToken(token, deviceId);
					return chain.filter(exchange);
				}

			} catch (ExpiredTokenException e) {
				return createErrorResponse(exchange.getResponse(), Error.EXPIRED_TOKEN);
			} catch (InvalidTokenException e) {
				return createErrorResponse(exchange.getResponse(), Error.INVALID_TOKEN);
			} catch (DifferentTokenVersionException e) {
				return createErrorResponse(exchange.getResponse(), Error.DIFFERENT_TOKEN_VERSION);
			}

			return chain.filter(exchange);
		};
	}

	private String generateDeviceId(ServerHttpRequest request) {
		return UUID.nameUUIDFromBytes(Objects.requireNonNull(request.getHeaders().getFirst(HttpHeaders.USER_AGENT)).getBytes()).toString();
	}

	private Mono<Void> createErrorResponse(ServerHttpResponse response, Error error) {

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
		//todo: yml 파일 말고 아마 여기에 권한별 라우팅 설정할것같은디..?
	}
}
