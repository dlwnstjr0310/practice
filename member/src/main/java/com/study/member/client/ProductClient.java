package com.study.member.client;

import com.study.member.client.fallback.ProductClientFallbackFactory;
import com.study.member.model.response.Response;
import com.study.member.model.response.product.ProductResponseDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CircuitBreaker(name = "product")
@FeignClient(name = "product-service", url = "${service.url.product}", fallbackFactory = ProductClientFallbackFactory.class)
public interface ProductClient {

	@GetMapping("/product/{id}")
	Response<ProductResponseDTO> getProductDetail(@PathVariable Long id);
}
