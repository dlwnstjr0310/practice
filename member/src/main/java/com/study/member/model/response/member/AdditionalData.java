package com.study.member.model.response.member;

import com.study.member.domain.entity.Address;
import com.study.member.domain.entity.WishList;

import java.util.List;

public record AdditionalData(

		List<AddressResponseDTO> addressList,

		List<WishListResponseDTO> wishList

) {
	public static AdditionalData of(List<Address> addressList,
	                                List<WishList> wishList) {
		return new AdditionalData(
				AddressResponseDTO.of(addressList),
				WishListResponseDTO.of(wishList)
		);
	}
}
