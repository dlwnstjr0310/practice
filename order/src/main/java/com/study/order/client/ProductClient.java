package com.study.order.client;

import com.study.order.model.response.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service", url = "${service.url.product}")
public interface ProductClient {

	@GetMapping("/product/list")
	List<ProductResponseDTO> getProductList(@RequestBody List<Long> idList);

}
