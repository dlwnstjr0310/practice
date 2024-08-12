package com.study.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final long TEMP_TEXT_EXPIRED_SECOND = 5L * 60L;
	private static final long ACCESS_TOKEN_EXPIRE_TIME_SECOND = 60L * 60L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME_SECOND = 14L * 24L * 60L * 60L;
	private final RedisTemplate<String, Map<String, String>> redisTemplate;

	public void storeTempCertificationTextInRedis(String key, String value) {

		Map<String, String> map = new HashMap<>();
		map.put(key, value);

		this.redisTemplate.opsForValue()
				.set(
						key,
						map,
						TEMP_TEXT_EXPIRED_SECOND,
						TimeUnit.SECONDS
				);
	}

	public String getTempCertificationTextInRedis(String email) {

		return Objects.requireNonNull(
				this.redisTemplate.opsForValue().get(email)
		).get(email);
	}

	public void storeRefreshTokenInRedis(String id, String accessToken, String refreshToken) {

		Map<String, String> map = this.redisTemplate.opsForValue().get(id);

		if (map == null) {
			map = new HashMap<>();
		}

		map.put(refreshToken, accessToken);

		this.redisTemplate.opsForValue()
				.set(
						id,
						map,
						REFRESH_TOKEN_EXPIRE_TIME_SECOND,
						TimeUnit.SECONDS
				);
	}

	public void deleteTargetInRedis(String key) {
		this.redisTemplate.delete(key);
	}

	public void deleteTargetInRedis(String key, String accessToken) {

		Map<String, String> map = this.redisTemplate.opsForValue().get(key);

		if (map != null) {
			map.forEach((k, v) -> {
				if (v.equals(accessToken)) {
					map.remove(k);
				}
			});

			this.redisTemplate.opsForValue().set(key, map);
		}
	}
}
