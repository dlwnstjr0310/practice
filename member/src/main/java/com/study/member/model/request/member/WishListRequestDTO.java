package com.study.member.model.request.member;

import com.study.member.domain.entity.WishList;
import com.study.member.domain.entity.member.Member;
import jakarta.validation.constraints.NotNull;

public record WishListRequestDTO(

		@NotNull
		Long productId,

		@NotNull
		Integer quantity

) {

	public WishList toEntity(Long memberId) {

		return WishList.builder()
				.member(Member.builder().id(memberId).build())
				.productId(productId)
				.quantity(quantity)
				.build();
	}
}
