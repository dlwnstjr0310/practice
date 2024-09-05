package com.study.member.auth;

import com.study.member.domain.entity.member.Member;
import com.study.member.exception.token.DifferentTokenVersionException;
import com.study.member.exception.token.ExpiredTokenException;
import com.study.member.exception.token.InvalidTokenException;
import com.study.member.model.response.member.TokenResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private static final String ISSUER = "ME";
	private static final String TOKEN_TYPE = "Bearer ";
	private static final String AUTHORITY_KEY = "auth";
	private static final String TOKEN_VERSION = "tokenVersion";

	private static final long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = 60L * 60L * 1000L;

	@Value("${SECURITY_JWT_KEY}")
	String secretKey;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public TokenResponseDTO generateTokenResponse(Member member) {
		return TokenResponseDTO.of(
				TOKEN_TYPE,
				(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME_MILLIS) / 1000,
				member
		);
	}

	public String generateToken(Member member, long expDate) {

		return Jwts.builder()
				.setIssuer(ISSUER)
				.setSubject(member.getId().toString())
				.setExpiration(new Date(new Date().getTime() + expDate))
				.claim(AUTHORITY_KEY, member.getMemberRole())
				.claim(TOKEN_VERSION, member.getTokenVersion())
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public void parseClaims(String token, Member member) {
		try {
			Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

			Integer tokenVersion = claims.get(TOKEN_VERSION, Integer.class);

			if (!tokenVersion.equals(member.getTokenVersion())) {
				throw new DifferentTokenVersionException(token);
			}
		} catch (ExpiredJwtException e) {
			throw new ExpiredTokenException(token);
		} catch (DifferentTokenVersionException e) {
			throw new DifferentTokenVersionException(token);
		} catch (Exception e) {
			throw new InvalidTokenException(token);
		}
	}
}
