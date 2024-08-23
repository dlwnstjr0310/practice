package com.study.product.controller;

import com.study.product.model.request.DiscountSaleProductRequestDTO;
import com.study.product.model.response.Response;
import com.study.product.service.ProductAsyncService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product/async")
public class ProductAsyncController {

	private final ProductAsyncService productAsyncService;

	@PostMapping("/set-up")
	public Response<Void> setUpDiscountSaleProduct(@Valid @RequestBody DiscountSaleProductRequestDTO request) {

		productAsyncService.setUpDiscountSaleProduct(request);
		return Response.<Void>builder()
				.build();
	}
}
