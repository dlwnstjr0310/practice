package com.study.member.model.response;

import java.util.List;

public record MemberResponseDTO(

		List<OrderResponseDTO> orderList,

		List<WishListResponseDTO> wishList
) {
}
