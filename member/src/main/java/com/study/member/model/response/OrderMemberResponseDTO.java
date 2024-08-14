package com.study.member.model.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderMemberResponseDTO(

		@NotNull
		Long id,

		@NotNull
		Integer totalPrice,

		@NotBlank
		String destinationAddress,

		@NotBlank
		String status

) {
}
