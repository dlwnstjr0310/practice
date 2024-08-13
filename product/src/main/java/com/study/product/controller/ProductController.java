package com.study.product.controller;

import com.study.product.controller.docs.ProductControllerDocs;
import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.model.response.Response;
import com.study.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	public Response<ProductResponseDTO> modifyProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {

		return Response.<ProductResponseDTO>builder()
				.data(productService.modifyProduct(id, request))
				.build();
	}

	//todo: 추후 페이징, 검색도 추가하기
	@GetMapping
	public Response<List<ProductResponseDTO>> getCurrentSaleProductList() {

		return Response.<List<ProductResponseDTO>>builder()
				.data(productService.getCurrentSaleProductList())
				.build();
	}

	@GetMapping("/{id}")
	public Response<ProductResponseDTO> getProductDetail(@PathVariable Long id) {

		return Response.<ProductResponseDTO>builder()
				.data(productService.getProductDetail(id))
				.build();
	}

	@GetMapping("/list")
	public Response<List<ProductResponseDTO>> getProductList(@RequestBody List<Long> idList) {

		return Response.<List<ProductResponseDTO>>builder()
				.data(productService.getProductList(idList))
				.build();
	}
}
