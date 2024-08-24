package com.study.product.controller.docs;

import com.study.product.model.request.ProductOrderRequestDTO;
import com.study.product.model.response.ProductOrderResponseDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "ProductOrder", description = "Order 모듈과 관련된 사용자 API")
public interface ProductOrderControllerDocs {

	@Operation(summary = "제품 주문", description = "제품을 주문하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "주문 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "재고가 부족합니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PatchMapping("/product/order")
	Response<List<ProductOrderResponseDTO>> modifyProductStock(@Valid @RequestBody List<ProductOrderRequestDTO> request);

	@Operation(summary = "제품 주문 전단계", description = "제품 주문 전단계에서 주문 가능 여부를 확인하고, 재고와 가격을 확인하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "주문 가능 여부 확인 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "주문 불가능한 제품이 포함되어 있습니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/product/order")
	Response<List<ProductOrderResponseDTO>> getProductOrderList(@Valid @RequestParam List<Long> productIdList);

	@Operation(summary = "제품 상세 조회", description = "특정 제품의 상세 정보를 조회하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "판매중인 상품이 아닙니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 제품입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/product/{id}")
	Response<ProductResponseDTO> getProductDetail(@PathVariable Long id);


}
