package com.study.order.client;

import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.ProductOrderResponseDTO;
import com.study.order.model.response.Response;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "product-service", url = "${service.url.product}")
public interface ProductClient {

	@PatchMapping("/product/order")
	Response<List<ProductOrderResponseDTO>> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> productList);

	@GetMapping("/product/order")
	Response<List<ProductOrderResponseDTO>> getProductOrderList(@Valid @RequestParam List<Long> productIdList);

}
