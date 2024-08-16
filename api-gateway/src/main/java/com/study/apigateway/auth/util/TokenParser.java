package com.study.apigateway.auth.util;

import com.study.apigateway.auth.exception.token.DifferentTokenVersionException;
import com.study.apigateway.auth.exception.token.ExpiredTokenException;
import com.study.apigateway.auth.exception.token.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenParser {

	private static final String TOKEN_TYPE = "Bearer ";
	private static final String AUTHORITY_KEY = "auth";
	private static final String TOKEN_VERSION = "tokenVersion";

	private final RedisService redisService;

	private SecretKey key;

	@Value("${SECURITY_JWT_KEY}")
	private String secretKey;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public SimpleGrantedAuthority isValidToken(String token, String deviceId) {
		try {

			String refreshToken = redisService.findRefreshTokenInRedis(deviceId).substring(TOKEN_TYPE.length());

			Claims accessTokenClaims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			Claims refreshTokenClaims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken).getBody();

			Integer accessTokenVersion = accessTokenClaims.get(TOKEN_VERSION, Integer.class);
			Integer refreshTokenVersion = refreshTokenClaims.get(TOKEN_VERSION, Integer.class);

			if (ObjectUtils.isEmpty(accessTokenVersion)) {
				throw new DifferentTokenVersionException(token);
			}

			if (!accessTokenVersion.equals(refreshTokenVersion)) {
				throw new DifferentTokenVersionException(token);
			}

			return new SimpleGrantedAuthority(accessTokenClaims.get(AUTHORITY_KEY).toString());
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException(token);
		} catch (DifferentTokenVersionException e) {
			throw new DifferentTokenVersionException(token);
		} catch (Exception e) {
			throw new InvalidTokenException(token);
		}
	}

	public String getJwt(ServerHttpRequest request) {
		String jwt = Optional.ofNullable(request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
				.orElseThrow(NullPointerException::new);

		if (!StringUtils.hasText(jwt) || !jwt.startsWith(TOKEN_TYPE)) {
			throw new InvalidTokenException(jwt);
		}

		return jwt.substring(TOKEN_TYPE.length());
	}

}
