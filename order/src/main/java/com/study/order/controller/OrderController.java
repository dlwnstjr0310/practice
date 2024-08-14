package com.study.order.controller;

import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.response.OrderResponseDTO;
import com.study.order.model.response.Response;
import com.study.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public Response<Void> createOrder(@Valid @RequestBody OrderRequestDTO request) {

		orderService.createOrder(request);
		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}

	//todo: 나중에 Member에서 mypage 에서 호출하기
//	@GetMapping
//	public Response<OrderResponseDTO> getOrderInfo() {
//
//		return Response.<OrderResponseDTO>builder()
//				.build();
//	}

	//todo: 아마.. 여기는 기간설정이나 페이징을 해야할듯
	@GetMapping("/{id}")
	public Response<OrderResponseDTO> getOrderList(@PathVariable Long id) {

		return Response.<OrderResponseDTO>builder()
				.data(orderService.getOrderList(id))
				.build();
	}

	@PatchMapping("/{id}")
	public Response<Void> modifyOrderStatus(@PathVariable Long id, @RequestParam String status) {

		orderService.modifyOrderStatus(id, status);
		return Response.<Void>builder()
				.build();
	}

}
