package com.study.apigateway.auth.jwt;

import com.study.apigateway.auth.exception.token.ExpiredTokenException;
import com.study.apigateway.auth.exception.token.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@RequiredArgsConstructor
public class TokenParser {

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
}
