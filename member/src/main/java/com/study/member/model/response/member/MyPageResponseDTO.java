package com.study.member.model.response.member;

import com.study.member.model.response.order.OrderResponseDTO;

import java.util.List;

public record MyPageResponseDTO(

		MemberInfoResponseDTO memberInfoResponseDTO,

		List<OrderResponseDTO> orderList

) {
	public static MyPageResponseDTO of(MemberInfoResponseDTO memberInfoResponseDTO,
	                                   List<OrderResponseDTO> orderList) {
		return new MyPageResponseDTO(
				memberInfoResponseDTO,
				orderList
		);
	}
}
