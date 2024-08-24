package com.study.member.client.fallback;

import com.study.member.client.ProductClient;
import com.study.member.exception.server.CircuitBreakerOpenException;
import com.study.member.exception.server.GatewayTimeoutException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.concurrent.TimeoutException;

public class ProductClientFallbackFactory implements FallbackFactory<ProductClient> {
	@Override
	public ProductClient create(Throwable cause) {
		return id -> {
			if (cause instanceof CallNotPermittedException) {
				throw new CircuitBreakerOpenException();
			} else if (cause instanceof TimeoutException) {
				throw new GatewayTimeoutException();
			} else {
				throw new InternalServerErrorException();
			}
		};
	}
}
