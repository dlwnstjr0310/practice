package com.study.member.client;

import com.study.member.exception.server.CircuitBreakerOpenException;
import com.study.member.exception.server.GatewayTimeoutException;
import com.study.member.model.response.order.OrderResponseDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.TimeoutException;

@CircuitBreaker(name = "order")
@FeignClient(name = "order-service", url = "${service.url.order}")
public interface OrderClient {

	@GetMapping("/order/member/{id}")
	@Retry(name = "orderClient", fallbackMethod = "orderFallback")
	List<OrderResponseDTO> getMemberOrderList(@PathVariable Long id);

	default List<OrderResponseDTO> orderFallback(Throwable cause) {
		if (cause instanceof CallNotPermittedException) {
			throw new CircuitBreakerOpenException();
		} else if (cause instanceof TimeoutException) {
			throw new GatewayTimeoutException();
		} else {
			throw new InternalServerErrorException();
		}
	}
}
