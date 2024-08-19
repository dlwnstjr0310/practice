package com.study.order.controller;

import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.response.Response;
import com.study.order.model.response.order.OrderResponseDTO;
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
