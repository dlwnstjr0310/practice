package com.study.order.model.response.order;

import com.study.order.domain.entity.OrderDetail;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderDetailResponseDTO(

		@NotNull
		Long productId,

		@NotNull
		Integer quantity,

		@NotNull
		Integer price

) {

	public static List<OrderDetailResponseDTO> of(List<OrderDetail> detailList) {

		return detailList.stream()
				.map(OrderDetailResponseDTO::of)
				.toList();
	}

	public static OrderDetailResponseDTO of(OrderDetail detail) {

		return new OrderDetailResponseDTO(
				detail.getProductId(),
				detail.getQuantity(),
				detail.getPrice()
		);
	}
}
