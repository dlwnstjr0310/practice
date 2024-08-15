package com.study.member.model.response;

import java.util.List;

public record OrderResponseDTO(

		Long id,

		String status,

		Integer totalPrice,

		String destinationAddress,

		List<OrderDetailResponseDTO> orderDetailList

) {
}
