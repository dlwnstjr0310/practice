package com.study.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

	private static final String TOKEN_TYPE = "Bearer ";
	private static final String SEPARATOR = ":REFRESH";
	private static final String BLACKLIST = "BLACKLIST";

	private static final long TEMP_TEXT_EXPIRED_SECOND = 5L * 60L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = 14L * 24L * 60L * 60L * 1000L;

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

	public void storeTokenVersionInRedis(String id, String tokenVersion) {
		redisTemplate.opsForValue().set(id, tokenVersion);
	}

	public void storeTokenInRedis(String id, String deviceId, String refreshToken) {
		redisTemplate.opsForValue().set(id + SEPARATOR + deviceId, deviceId);
		redisTemplate.opsForValue().set(deviceId, refreshToken, REFRESH_TOKEN_EXPIRE_TIME_MILLIS, TimeUnit.MILLISECONDS);
	}

	public String findRefreshTokenInRedis(String deviceId) {
		Object value = redisTemplate.opsForValue().get(deviceId);
		return value != null ? value.toString() : null;
	}

	public void deleteAllTokenInRedis(String id) {
		Set<String> keys = redisTemplate.keys(id + SEPARATOR + "*");

		if (keys != null && !keys.isEmpty()) {
			for (String key : keys) {
				String deviceId = (String) redisTemplate.opsForValue().get(key);

				if (deviceId != null) {
					redisTemplate.delete(deviceId);
				}
			}
			redisTemplate.delete(keys);
		}
	}

	public void deleteTokenInRedis(String id, String deviceId) {
		redisTemplate.delete(id + SEPARATOR + deviceId);

		String token = (String) redisTemplate.opsForValue().get(deviceId);
		Long expire = redisTemplate.getExpire(deviceId, TimeUnit.MILLISECONDS);

		redisTemplate.delete(deviceId);

		if (token != null) {
			storeBlackListInRedis(token, expire);
		}
	}

	public void storeBlackListInRedis(String token, Long expiresIn) {
		redisTemplate.opsForValue().set(token.substring(TOKEN_TYPE.length()), BLACKLIST, expiresIn, TimeUnit.MILLISECONDS);
	}

	public boolean isBlackList(String token) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(token.substring(TOKEN_TYPE.length())));
	}
}
