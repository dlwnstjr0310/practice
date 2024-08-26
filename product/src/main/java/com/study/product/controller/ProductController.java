package com.study.product.controller;

import com.study.product.controller.docs.ProductControllerDocs;
import com.study.product.model.request.DiscountSaleProductRequestDTO;
import com.study.product.model.request.ProductOrderRequestDTO;
import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductOrderResponseDTO;
import com.study.product.model.response.ProductSearchResultDTO;
import com.study.product.model.response.Response;
import com.study.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController implements ProductControllerDocs {

	private final ProductService productService;

	@PostMapping
	public Response<Long> createProduct(@Valid @RequestBody ProductRequestDTO request) {

		return Response.<Long>builder()
				.data(productService.createProduct(request))
				.build();
	}

	@PostMapping("/set-up")
	public Response<Void> setUpDiscountSaleProduct(@Valid @RequestBody DiscountSaleProductRequestDTO request) {

		productService.setUpDiscountSaleProduct(request);
		return Response.<Void>builder()
				.build();
	}

	@PatchMapping("/{id}")
	public Response<Long> modifyProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {

		return Response.<Long>builder()
				.data(productService.modifyProduct(id, request))
				.build();
	}

	@PatchMapping("/order")
	public List<ProductOrderResponseDTO> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> request) {

		return productService.modifyProductStock(request);
	}

	@GetMapping
	public Response<ProductSearchResultDTO> getCurrentSaleProductList(Pageable pageable,
	                                                                  SearchConditionDTO searchCondition) {

		return Response.<ProductSearchResultDTO>builder()
				.data(productService.getCurrentSaleProductList(pageable, searchCondition))
				.build();
	}

	@GetMapping("/order")
	public List<ProductOrderResponseDTO> getProductOrderList(@Valid @RequestParam List<Long> productIdList) {

		return productService.getProductOrderList(productIdList);
	}
}
