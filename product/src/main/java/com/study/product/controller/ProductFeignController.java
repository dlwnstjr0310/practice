package com.study.product.controller;

import com.study.product.controller.docs.ProductOrderControllerDocs;
import com.study.product.model.request.ProductOrderRequestDTO;
import com.study.product.model.response.ProductOrderResponseDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.model.response.Response;
import com.study.product.service.ProductFeignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductFeignController implements ProductOrderControllerDocs {

	private final ProductFeignService productFeignService;

	@PatchMapping("/order")
	public Response<List<ProductOrderResponseDTO>> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> request) {

		return Response.<List<ProductOrderResponseDTO>>builder()
				.data(productFeignService.modifyProductStock(request))
				.build();
	}

	@GetMapping("/order")
	public Response<List<ProductOrderResponseDTO>> getProductOrderList(@Valid @RequestParam List<Long> productIdList) {

		return Response.<List<ProductOrderResponseDTO>>builder()
				.data(productFeignService.getProductOrderList(productIdList))
				.build();
	}

	@GetMapping("/{id}")
	public Response<ProductResponseDTO> getProductDetail(@PathVariable Long id) {

		return Response.<ProductResponseDTO>builder()
				.data(productFeignService.getProductDetail(id))
				.build();
	}

}
