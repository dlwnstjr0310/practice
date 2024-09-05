package com.study.product.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final Duration EXPIRATION_DURATION = Duration.ofDays(7);

	private final RedisTemplate<String, Object> redisTemplate;

	public void storeInRedis(String key, String value) {
		redisTemplate.opsForValue().set(
				key,
				value,
				EXPIRATION_DURATION
		);
	}

}
