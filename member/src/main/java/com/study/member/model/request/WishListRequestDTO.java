package com.study.member.model.request;

import com.study.member.domain.entity.WishList;
import com.study.member.domain.entity.member.Member;
import jakarta.validation.constraints.NotNull;

public record WishListRequestDTO(

		@NotNull
		Long productId,

		@NotNull
		Integer quantity

) {

	public WishList toEntity(Integer price, Long memberId) {

		return WishList.builder()
				.productId(productId)
				.quantity(quantity)
				.price(price)
				.member(Member.builder().id(memberId).build())
				.build();
	}
}
