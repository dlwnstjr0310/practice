package com.study.member.model.response.member;

import com.study.member.model.response.order.OrderResponseDTO;

import java.util.List;

public record MemberResponseDTO(

		List<OrderResponseDTO> orderList,

		List<WishListResponseDTO> wishList
) {
	public static MemberResponseDTO of(List<OrderResponseDTO> orderList,
	                                   List<WishListResponseDTO> wishList) {
		return new MemberResponseDTO(orderList, wishList);
	}
}
