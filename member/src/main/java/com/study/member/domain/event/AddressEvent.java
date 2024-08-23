package com.study.member.domain.event;

public record AddressEvent(

		Long memberId,

		String addressAlias,

		String destinationAddress,

		String zipCode,

		String phone
) {
}
