package com.study.member.model.response;

public record OrderDetailResponseDTO(

		Long id,

		Long productId,

		Integer quantity,

		Integer price
) {
}
