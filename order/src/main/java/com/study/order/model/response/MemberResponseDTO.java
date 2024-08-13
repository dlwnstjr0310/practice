package com.study.order.model.response;

import java.util.List;

public record MemberResponseDTO(

		Long id,

		String name,

		String email,

		List<AddressResponseDTO> addressList
) {

}
