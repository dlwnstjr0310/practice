package com.study.member.model.response.member;

import com.study.member.domain.entity.WishList;

import java.util.List;

public record WishListResponseDTO(

		Long id,

		Long productId,

		Integer quantity,

		Integer price
) {

	public static List<WishListResponseDTO> of(List<WishList> wishList) {
		return wishList.stream()
				.map(WishListResponseDTO::of)
				.toList();
	}

	public static WishListResponseDTO of(WishList wishList) {
		return new WishListResponseDTO(
				wishList.getId(),
				wishList.getProductId(),
				wishList.getQuantity(),
				wishList.getPrice()
		);
	}
}
