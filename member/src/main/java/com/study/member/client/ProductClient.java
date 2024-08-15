package com.study.member.client;

import com.study.member.model.response.ProductResponseDTO;
import com.study.member.model.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${service.url.product}")
public interface ProductClient {

	@GetMapping("/product/{id}")
	Response<ProductResponseDTO> getProductDetail(@PathVariable Long id);
}
