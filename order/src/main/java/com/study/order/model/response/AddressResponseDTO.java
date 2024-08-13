package com.study.order.model.response;

public record AddressResponseDTO(

		Long id,

		String name,

		String address,

		String zipCode,

		String phone,

		Boolean isDefault
) {
}
