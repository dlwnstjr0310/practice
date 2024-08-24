package com.study.product.controller.docs;

import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductSearchResultDTO;
import com.study.product.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "제품 등록,수정 및 조회 등의 사용자 API")
public interface ProductControllerDocs {

	@Operation(summary = "제품 등록", description = "신규 제품을 등록하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "등록 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/product")
	Response<Long> createProduct(@Valid @RequestBody ProductRequestDTO request);

	@Operation(summary = "제품 수정", description = "기존 제품을 수정하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 제품입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PatchMapping("/product/{id}")
	Response<Long> modifyProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request);

	@Operation(summary = "제품 목록 조회", description = "현재 판매중인 제품 목록을 조회하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/product")
	Response<ProductSearchResultDTO> getCurrentSaleProductList(
			Pageable pageable,
			SearchConditionDTO searchCondition
	);

}
