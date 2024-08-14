package com.study.member.model.response;

public record WishListResponseDTO(

		Long id,

		Long productId,

		Integer quantity,

		Integer price
) {
}
