package com.study.product.controller;

import com.study.product.controller.docs.ProductControllerDocs;
import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductPaginationResponseDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.model.response.Response;
import com.study.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController implements ProductControllerDocs {

	private final ProductService productService;

	//todo: 권한 추가하기
	@PostMapping
	public Response<Long> createProduct(@Valid @RequestBody ProductRequestDTO request) {

		return Response.<Long>builder()
				.data(productService.createProduct(request))
				.build();
	}

	@PatchMapping("/{id}")
	public Response<Long> modifyProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {

		return Response.<Long>builder()
				.data(productService.modifyProduct(id, request))
				.build();
	}

	@GetMapping
	public Response<ProductPaginationResponseDTO> getCurrentSaleProductList(Pageable pageable,
	                                                                        SearchConditionDTO searchCondition) {

		return Response.<ProductPaginationResponseDTO>builder()
				.data(productService.getCurrentSaleProductList(pageable, searchCondition))
				.build();
	}

	@GetMapping("/{id}")
	public Response<ProductResponseDTO> getProductDetail(@PathVariable Long id) {

		return Response.<ProductResponseDTO>builder()
				.data(productService.getProductDetail(id))
				.build();
	}
}
