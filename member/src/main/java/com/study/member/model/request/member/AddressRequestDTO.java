package com.study.member.model.request.member;

import com.study.member.domain.entity.Address;
import com.study.member.domain.entity.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressRequestDTO(

		@NotNull
		Long memberId,

		@NotNull
		String addressAlias,

		@NotBlank
		String address,

		@NotBlank
		String zipCode,

		@NotBlank
		String phone,

		@NotNull
		Boolean isDefault
) {
	public Address toEntity() {
		return Address.builder()
				.name(addressAlias)
				.address(address)
				.zipCode(zipCode)
				.phone(phone)
				.isDefault(isDefault)
				.member(Member.builder().id(memberId).build())
				.build();
	}
}
