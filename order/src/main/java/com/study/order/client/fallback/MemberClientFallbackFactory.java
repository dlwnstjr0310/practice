package com.study.order.client.fallback;

import com.study.order.client.MemberClient;
import com.study.order.exception.server.CircuitBreakerOpenException;
import com.study.order.exception.server.GatewayTimeoutException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import jakarta.ws.rs.InternalServerErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class MemberClientFallbackFactory implements FallbackFactory<MemberClient> {

	@Override
	public MemberClient create(Throwable cause) {
		return request -> {
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
