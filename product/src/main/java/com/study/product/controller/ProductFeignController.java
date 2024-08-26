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
	public List<ProductOrderResponseDTO> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> request) {

		return productFeignService.modifyProductStock(request);
	}

	@GetMapping("/order")
	public List<ProductOrderResponseDTO> getProductOrderList(@Valid @RequestParam List<Long> productIdList) {

		return productFeignService.getProductOrderList(productIdList);
	}

	@GetMapping("/{id}")
	public Response<ProductResponseDTO> getProductDetail(@PathVariable Long id) {

		return Response.<ProductResponseDTO>builder()
				.data(productFeignService.getProductDetail(id))
				.build();
	}

}
