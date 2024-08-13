package com.study.member.model.response;

import com.study.member.domain.entity.member.Member;

import java.util.List;

public record MemberResponseDTO(

		Long id,

		String name,

		String email,

		List<AddressResponseDTO> addressList
) {

	public static MemberResponseDTO of(Member member) {
		return new MemberResponseDTO(
				member.getId(),
				member.getName(),
				member.getEmail(),
				AddressResponseDTO.of(member.getAddressList())
		);
	}
}
