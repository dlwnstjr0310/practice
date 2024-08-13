package com.study.order.model.response;

import com.study.order.domain.entity.order.Order;

import java.util.List;

public record OrderResponseDTO(

		Long id,

		Integer totalPrice,

		String destinationAddress,

		String status,

		List<OrderDetailResponseDTO> detailList

) {

	public static OrderResponseDTO of(Order order) {
		return new OrderResponseDTO(
				order.getId(),
				order.getTotalPrice(),
				order.getDestinationAddress(),
				order.getStatus().name(),
				OrderDetailResponseDTO.of(order.getDetailList())
		);
	}
}
