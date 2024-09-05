package com.study.member.model.response.order;

public record OrderDetailResponseDTO(

		Long id,

		Long productId,

		Integer quantity,

		Integer price
) {
}
