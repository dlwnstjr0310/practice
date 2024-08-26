package com.study.order.client;

import com.study.order.client.fallback.ProductClientFallbackFactory;
import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.order.ProductOrderResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@CircuitBreaker(name = "product")
@FeignClient(name = "product-service", url = "${service.url.product}", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductClient {

	@PatchMapping("/product/order")
	List<ProductOrderResponseDTO> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> productList);

	@GetMapping("/product/order")
	List<ProductOrderResponseDTO> getProductOrderList(@Valid @RequestParam List<Long> productIdList);

}
