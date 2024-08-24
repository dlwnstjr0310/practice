package com.study.member.client;

import com.study.member.client.fallback.OrderClientFallbackFactory;
import com.study.member.model.response.order.OrderResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@CircuitBreaker(name = "order")
@FeignClient(name = "order-service", url = "${service.url.order}", fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient {

	@GetMapping("/order/member/{id}")
	List<OrderResponseDTO> getMemberOrderList(@PathVariable Long id);
}
