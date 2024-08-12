package com.study.web.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final long TEMP_TEXT_EXPIRED_SECOND = 5L * 60L;
	private final RedisTemplate<String, Object> redisTemplate;

	public void storeTempCertificationTextInRedis(String email, String certificationText) {

		redisTemplate.opsForValue()
				.set(
						email,
						certificationText,
						TEMP_TEXT_EXPIRED_SECOND,
						TimeUnit.SECONDS
				);
	}

	public String getTempCertificationTextInRedis(String email) {

		return Objects.requireNonNull(
				redisTemplate.opsForValue().get(email)
		).toString();
	}

	public void storeRefreshTokenInRedis(String id, String accessToken, String refreshToken) {

		Map<Object, Object> map = redisTemplate.opsForHash().entries(id);

		if (map.isEmpty()) {
			map = new HashMap<>();
		}

		map.put(refreshToken, accessToken);

		// 유효기간은 토큰 까봐야 알수있음
		redisTemplate.opsForHash().putAll(id, map);
	}

	public void deleteTargetInRedis(String key) {
		redisTemplate.delete(key);
	}

	public void deleteTargetInRedis(String key, String accessToken) {

		List<Object> removeList = new ArrayList<>();

		redisTemplate.opsForHash().entries(key).forEach((k, v) -> {
			if (v.toString().equals(accessToken)) {
				removeList.add(k);
			}
		});

		if (!removeList.isEmpty()) {
			removeList.forEach(token ->
					redisTemplate.opsForHash().delete(key, token)
			);
		}
	}
}
