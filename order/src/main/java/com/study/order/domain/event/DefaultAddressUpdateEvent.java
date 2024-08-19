package com.study.order.domain.event;

public record DefaultAddressUpdateEvent(

		Long memberId,

		String addressAlias,

		String destinationAddress,

		String zipCode,

		String phone

) {
}
