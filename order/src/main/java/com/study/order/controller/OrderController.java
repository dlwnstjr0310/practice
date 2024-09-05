package com.study.order.controller;

import com.study.order.controller.docs.OrderControllerDocs;
import com.study.order.model.request.DiscountProductOrderRequestDTO;
import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.response.Response;
import com.study.order.model.response.order.OrderResponseDTO;
import com.study.order.service.OrderEventProducer;
import com.study.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController implements OrderControllerDocs {

	private final OrderService orderService;
	private final OrderEventProducer orderEventProducer;

	@PostMapping
	public Response<Void> createOrder(@Valid @RequestBody OrderRequestDTO request) {

		orderService.createOrder(request);
		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}

	@PostMapping("/discount")
	public Response<Void> createDiscountProductOrder(@Valid @RequestBody DiscountProductOrderRequestDTO request) {

		orderEventProducer.createOrder(request);
		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}

	@PatchMapping("/{id}")
	public Response<Void> modifyOrderStatus(@PathVariable Long id, @RequestParam String status) {

		orderEventProducer.modifyOrderStatus(id, status);
		return Response.<Void>builder()
				.build();
	}

	@GetMapping("/member/{id}")
	public List<OrderResponseDTO> getMemberOrderList(@PathVariable Long id) {

		return orderService.getMemberOrderList(id);
	}

	@GetMapping("/{id}")
	public Response<OrderResponseDTO> getOrderDetailList(@PathVariable Long id) {

		return Response.<OrderResponseDTO>builder()
				.data(orderService.getOrderDetailList(id))
				.build();
	}
}
