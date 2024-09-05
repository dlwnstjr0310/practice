package com.study.apigateway.auth.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final String SEPARATOR = ":REFRESH";
	private static final String BLACKLIST = "BLACKLIST";

	private final RedisTemplate<String, Object> redisTemplate;

	public String findRefreshTokenInRedis(String deviceId) {
		Object value = redisTemplate.opsForValue().get(deviceId);
		return value != null ? value.toString() : null;
	}

	public void storeBlackListInRedis(String token, Long expiresIn) {
		redisTemplate.opsForValue().set(token, BLACKLIST, expiresIn, TimeUnit.MILLISECONDS);
	}

	public boolean isBlackList(String token) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(token));
	}
}
