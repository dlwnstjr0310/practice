package com.study.order.model.response.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MemberResponseDTO(

		@NotNull
		Long id,

		@NotBlank
		String name,

		@NotBlank
		String email,

		@NotNull
		List<AddressResponseDTO> addressList
) {

}
