package com.study.order.model.response;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderResponseDTO(

		@NotNull
		Long id,

		@NotNull
		Integer totalPrice,

		@NotBlank
		String destinationAddress,

		@NotBlank
		String status,

		@NotNull
		List<OrderDetailResponseDTO> detailList

) {

	public static OrderResponseDTO of(Order order, List<OrderDetail> orderDetailList) {
		return new OrderResponseDTO(
				order.getId(),
				order.getTotalPrice(),
				order.getDestinationAddress(),
				order.getStatus().name(),
				OrderDetailResponseDTO.of(orderDetailList)
		);
	}
}
