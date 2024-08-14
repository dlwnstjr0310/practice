package com.study.order.model.response;

import com.study.order.domain.entity.order.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderMemberResponseDTO(

		@NotNull
		Long id,

		@NotNull
		Integer totalPrice,

		@NotBlank
		String destinationAddress,

		@NotBlank
		String status

) {

	public static OrderMemberResponseDTO of(Order order) {
		return new OrderMemberResponseDTO(
				order.getId(),
				order.getTotalPrice(),
				order.getDestinationAddress(),
				order.getStatus().name()
		);
	}
}
