package com.study.order.controller.docs;

import com.study.order.model.response.Response;
import com.study.order.model.response.order.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Order", description = "Member 모듈과 관련된 사용자 API")
public interface OrderMemberControllerDocs {

	@Operation(summary = "회원 주문 목록 조회", description = "회원의 주문 목록을 조회하는 API.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/order/member/{id}")
	List<OrderResponseDTO> getMemberOrderList(@PathVariable Long id);
}
