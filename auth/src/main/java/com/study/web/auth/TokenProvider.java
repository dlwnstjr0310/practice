package com.study.web.auth;

import com.study.web.domain.entity.Member;
import com.study.web.exception.token.ExpiredTokenException;
import com.study.web.exception.token.InvalidTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private static final String ISSUER = "HELLO";
	private static final String TOKEN_TYPE = "Bearer";
	private static final String AUTHORITY_KEY = "auth";

	@Value("${SECURITY_JWT_KEY}")
	String secretKey;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public String generateToken(Member member, long expDate) {

		return Jwts.builder()
				.setIssuer(ISSUER)
				.setSubject(member.getId().toString())
				.setExpiration(new Date(new Date().getTime() + expDate))
				.claim(AUTHORITY_KEY, member.getMemberRole())
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String getJwt(HttpServletRequest request) {
		String jwt = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
				.orElseThrow(NullPointerException::new);

		if (!StringUtils.hasText(jwt) || !jwt.startsWith(TOKEN_TYPE)) {
			throw new InvalidTokenException(jwt);
		}

		return jwt.substring(7);
	}

	public void parseClaims(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException(token);
		} catch (Exception e) {
			throw new InvalidTokenException(token);
		}
	}

}
