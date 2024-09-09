package com.study.order.client;

import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.exception.server.CircuitBreakerOpenException;
import com.study.order.exception.server.TimeoutException;
import com.study.order.model.response.Response;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "member-server")
public interface MemberClient {

	@PostMapping("/member/address")
	@CircuitBreaker(name = "member")
	@Retry(name = "memberClient", fallbackMethod = "memberFallback")
	Response<Void> updateAddress(@RequestBody AddressEvent request);

	// 서킷브레이커가 열려있지 않은 상태에서만 동작함
	default Response<Void> memberFallback(Throwable cause) {
		if (cause instanceof CallNotPermittedException) {
			throw new CircuitBreakerOpenException();
		} else if (cause instanceof java.util.concurrent.TimeoutException) {
			throw new TimeoutException();
		} else {
			throw new InternalServerErrorException();
		}
	}
}
