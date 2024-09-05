package com.study.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final Duration EXPIRATION_DURATION = Duration.ofDays(1);

	private final RedisTemplate<String, Object> redisTemplate;

	public String getValue(String key) {
		return Objects.requireNonNull(redisTemplate.opsForValue().get(key)).toString();
	}

	public void storeInRedis(String key, String value) {
		redisTemplate.opsForValue().set(
				key,
				value,
				EXPIRATION_DURATION
		);
	}

}
