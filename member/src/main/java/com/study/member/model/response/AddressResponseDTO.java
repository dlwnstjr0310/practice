package com.study.member.model.response;

import com.study.member.domain.entity.Address;

import java.util.List;
import java.util.Set;

public record AddressResponseDTO(

		Long id,

		String name,

		String address,

		String zipCode,

		String phone,

		Boolean isDefault
) {
	public static List<AddressResponseDTO> of(Set<Address> addressList) {
		return addressList.stream().map(AddressResponseDTO::of).toList();
	}

	public static AddressResponseDTO of(Address address) {

		return new AddressResponseDTO(
				address.getId(),
				address.getName(),
				address.getAddress(),
				address.getZipCode(),
				address.getPhone(),
				address.getIsDefault()
		);
	}
}
