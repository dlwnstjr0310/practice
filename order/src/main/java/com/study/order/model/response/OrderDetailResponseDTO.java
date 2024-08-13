package com.study.order.model.response;

import com.study.order.domain.entity.OrderDetail;

import java.util.List;
import java.util.Set;

public record OrderDetailResponseDTO(

		Long productId,

		Integer quantity,

		Integer price

) {

	public static List<OrderDetailResponseDTO> of(Set<OrderDetail> detailList) {

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
