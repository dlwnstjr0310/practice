package com.study.member.model.response.member;

import com.study.member.domain.entity.member.Member;
import com.study.member.model.response.order.OrderResponseDTO;

import java.util.List;

public record MemberResponseDTO(

		Long id,

		String name,

		String email,

		AdditionalData additionalData,

		List<OrderResponseDTO> orderList

) {
	public static MemberResponseDTO of(Member member,
	                                   AdditionalData additionalData,
	                                   List<OrderResponseDTO> orderList) {
		return new MemberResponseDTO(
				member.getId(),
				member.getName(),
				member.getEmail(),
				additionalData,
				orderList
		);
	}
}
