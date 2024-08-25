package com.study.member.model.response.member;

import com.study.member.domain.entity.Address;
import com.study.member.domain.entity.WishList;
import com.study.member.domain.entity.member.Member;

import java.util.List;

public record MemberInfoResponseDTO(

		Long id,

		String name,

		String email,

		List<AddressResponseDTO> addressList,

		List<WishListResponseDTO> wishList

) {
	public static MemberInfoResponseDTO of(Member member,
	                                       List<Address> addressList,
	                                       List<WishList> wishList) {
		return new MemberInfoResponseDTO(
				member.getId(),
				member.getName(),
				member.getEmail(),
				AddressResponseDTO.of(addressList),
				WishListResponseDTO.of(wishList)
		);
	}
}
