package com.study.apigateway.auth.filter;

import com.study.apigateway.auth.exception.Error;
import com.study.apigateway.auth.exception.token.DifferentTokenVersionException;
import com.study.apigateway.auth.exception.token.ExpiredTokenException;
import com.study.apigateway.auth.exception.token.InvalidTokenException;
import com.study.apigateway.auth.exception.token.RegisteredInBlackListException;
import com.study.apigateway.auth.util.TokenParser;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

import static com.study.apigateway.auth.exception.handler.FilterExceptionHandler.createErrorResponse;

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
			} catch (DifferentTokenVersionException e) {
				return createErrorResponse(exchange.getResponse(), Error.DIFFERENT_TOKEN_VERSION);
			} catch (RegisteredInBlackListException e) {
				return createErrorResponse(exchange.getResponse(), Error.REGISTERED_IN_BLACKLIST);
			} catch (InvalidTokenException e) {
				return createErrorResponse(exchange.getResponse(), Error.INVALID_TOKEN);
			}

			return chain.filter(exchange);
		};
	}

	private String generateDeviceId(ServerHttpRequest request) {
		return UUID.nameUUIDFromBytes(Objects.requireNonNull(request.getHeaders().getFirst(HttpHeaders.USER_AGENT)).getBytes()).toString();
	}

	public static class Config {
		//todo: yml 파일 말고 아마 여기에 권한별 라우팅 설정할것같은디..?
	}
}
