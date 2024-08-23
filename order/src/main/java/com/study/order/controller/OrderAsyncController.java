package com.study.order.controller;

import com.study.order.model.request.DiscountProductOrderRequestDTO;
import com.study.order.model.response.Response;
import com.study.order.service.OrderEventProducer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order/async")
public class OrderAsyncController {

	private final OrderEventProducer orderEventProducer;

	@PostMapping
	public Response<Void> createOrder(@Valid @RequestBody DiscountProductOrderRequestDTO request) {

		orderEventProducer.createOrder(request);
		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}

}
