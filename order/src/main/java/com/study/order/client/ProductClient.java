package com.study.order.client;

import com.study.order.exception.order.OutOfStockException;
import com.study.order.exception.server.CircuitBreakerOpenException;
import com.study.order.exception.server.TimeoutException;
import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.order.ProductOrderResponseDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CircuitBreaker(name = "product")
@FeignClient(name = "product-server")
public interface ProductClient {

	@PatchMapping("/product/order")
	@Retry(name = "productClient", fallbackMethod = "productFallback")
	List<ProductOrderResponseDTO> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> productList);

	@GetMapping("/product/order")
	@Retry(name = "productClient", fallbackMethod = "productFallback")
	List<ProductOrderResponseDTO> getProductOrderList(@Valid @RequestParam List<Long> productIdList);

	default List<ProductOrderResponseDTO> productFallback(Throwable cause) {
		if (cause instanceof OutOfStockException) {
			throw new OutOfStockException();
		} else if (cause instanceof CallNotPermittedException) {
			throw new CircuitBreakerOpenException();
		} else if (cause instanceof java.util.concurrent.TimeoutException) {
			throw new TimeoutException();
		} else {
			throw new InternalServerErrorException();
		}
	}
}
