package com.study.apigateway.auth.jwt;

import com.study.apigateway.auth.exception.token.ExpiredTokenException;
import com.study.apigateway.auth.exception.token.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenParser {

	private static final String TOKEN_TYPE = "Bearer";

	private SecretKey key;

	@Value("${SECURITY_JWT_KEY}")
	private String secretKey;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public boolean isValidToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
			return true;
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException(token);
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

		return jwt.substring(7);
	}

}
