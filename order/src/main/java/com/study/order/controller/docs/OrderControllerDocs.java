package com.study.order.controller.docs;

import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.response.OrderResponseDTO;
import com.study.order.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "상품 구매, 주문내역 조회,수정 등의 사용자 API")
public interface OrderControllerDocs {

	@Operation(summary = "상품 구매", description = "사용자의 ID 를 통해 주문을 생성하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "주문 생성 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/order")
	Response<Void> createOrder(@Valid @RequestBody OrderRequestDTO request);

	@Operation(summary = "주문 내역 조회", description = "사용자의 ID 를 통해 주문 내역을 조회하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 주문입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/{id}")
	Response<OrderResponseDTO> getOrderList(@PathVariable Long id);

	@Operation(summary = "주문 내역 수정", description = "주문 전체의 상태를 일괄적으로 변경하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = """
					1.이미 배송중인 상품입니다. \n
					2.반품 기간이 지났습니다.
					""", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 주문입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PatchMapping("/order")
	Response<Void> modifyOrder(@Valid @RequestBody List<Long> orderIdList, @RequestParam String status);

}
