package com.study.member.client.fallback;

import com.study.member.client.OrderClient;
import com.study.member.exception.server.CircuitBreakerOpenException;
import com.study.member.exception.server.GatewayTimeoutException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.concurrent.TimeoutException;

public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {

	@Override
	public OrderClient create(Throwable cause) {

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
