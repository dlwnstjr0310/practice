package com.study.web.service;

import com.study.web.model.request.MemberRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final long TEMP_TEXT_EXPIRED_SECOND = 5L * 60L;
	private static final long ACCESS_TOKEN_EXPIRE_TIME_SECOND = 60L * 60L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME_SECOND = 14L * 24L * 60L * 60L;
	private final StringRedisTemplate redisTemplate;

	public void storeTempCertificationTextInRedis(String id, String text) {

		this.redisTemplate.opsForValue()
				.set(
						id,
						text,
						TEMP_TEXT_EXPIRED_SECOND,
						TimeUnit.SECONDS
				);
	}

	public String getTempCertificationTextInRedis(MemberRequest.Verify request) {

		return this.redisTemplate.opsForValue().get(request.email());
	}

	public void storeRefreshTokenInRedis(String id, String token) {

		this.redisTemplate.opsForValue()
				.set(
						id,
						token,
						REFRESH_TOKEN_EXPIRE_TIME_SECOND,
						TimeUnit.SECONDS
				);
	}

	public String getRefreshTokenInRedis(String id) {
		return this.redisTemplate.opsForValue().get(id);
	}

	public void deleteTargetInRedis(String id) {
		this.redisTemplate.delete(id);
	}
}
