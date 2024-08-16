package com.study.auth.auth;

import com.study.auth.domain.entity.Member;
import com.study.auth.exception.token.DifferentTokenVersionException;
import com.study.auth.exception.token.ExpiredTokenException;
import com.study.auth.exception.token.InvalidTokenException;
import com.study.auth.model.response.MemberResponseDTO;
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
import org.springframework.util.StringUtils;

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

	public MemberResponseDTO generateTokenResponse(Member member) {
		return MemberResponseDTO.of(
				TOKEN_TYPE,
				(new Date().getTime() + ACCESS_TOKEN_EXPIRE_TIME_MILLIS) / 1000,
				member
		);
	}

	public String generateToken(Member member, long expDate) {

		return TOKEN_TYPE + Jwts.builder()
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

	public String getJwt(String jwt) {

		if (!StringUtils.hasText(jwt) || !jwt.startsWith(TOKEN_TYPE)) {
			throw new InvalidTokenException(jwt);
		}

		return jwt.substring(TOKEN_TYPE.length());
	}

}
