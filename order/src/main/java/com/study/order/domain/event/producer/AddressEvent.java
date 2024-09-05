package com.study.order.domain.event.producer;

public record AddressEvent(

		Long memberId,

		String addressAlias,

		String destinationAddress,

		String zipCode,

		String phone

) {
}
