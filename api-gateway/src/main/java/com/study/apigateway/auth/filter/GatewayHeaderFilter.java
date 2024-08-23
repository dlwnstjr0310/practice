package com.study.apigateway.auth.filter;

import com.study.apigateway.auth.util.TokenParser;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class GatewayHeaderFilter extends AbstractGatewayFilterFactory<GatewayHeaderFilter.Config> {

	private static final String[] EXCLUDED_PATHS = {"/", "/member/join", "/member/mail-certification/send", "/member/mail-certification/verify", "/member/login"};

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

//			try { 테스트용 비활성화
//
//				String token = tokenParser.getJwt(exchange.getRequest());
//				String deviceId = generateDeviceId(exchange.getRequest());
//
//				if (!ObjectUtils.isEmpty(token)) {
//
//					// todo: 이걸로 권한별 라우팅 설정하기. 좀만있다가../ 근데 createResponse 작동 안하는것같기도
//					SimpleGrantedAuthority role = tokenParser.isValidToken(token, deviceId);
//					return chain.filter(exchange);
//				}
//
//			} catch (ExpiredTokenException e) {
//				return createErrorResponse(exchange.getResponse(), Error.EXPIRED_TOKEN);
//			} catch (DifferentTokenVersionException e) {
//				return createErrorResponse(exchange.getResponse(), Error.DIFFERENT_TOKEN_VERSION);
//			} catch (RegisteredInBlackListException e) {
//				return createErrorResponse(exchange.getResponse(), Error.REGISTERED_IN_BLACKLIST);
//			} catch (Exception e) {
//				e.printStackTrace();
//				return createErrorResponse(exchange.getResponse(), Error.INVALID_TOKEN);
//			}

			return chain.filter(exchange);
		};
	}

	private String generateDeviceId(ServerHttpRequest request) {
		return UUID.nameUUIDFromBytes(Objects.requireNonNull(request.getHeaders().getFirst(HttpHeaders.USER_AGENT)).getBytes()).toString();
	}

	public static class Config {
	}
}
