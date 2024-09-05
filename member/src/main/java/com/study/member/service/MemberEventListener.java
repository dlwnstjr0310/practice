package com.study.member.service;

import com.study.member.domain.event.AddressEvent;
import com.study.member.model.request.member.AddressRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEventListener {

	private final MemberService memberService;

	@KafkaListener(topics = "address-update-event")
	public void handleAddressUpdateEvent(AddressEvent event) {

		memberService.createAddress(
				new AddressRequestDTO(
						event.memberId(),
						event.addressAlias(),
						event.destinationAddress(),
						event.zipCode(),
						event.phone(),
						true
				)
		);
	}

	@KafkaListener(topics = "address-store-event")
	public void handlerAddressStoreEvent(AddressEvent event) {

		memberService.createAddress(
				new AddressRequestDTO(
						event.memberId(),
						event.addressAlias(),
						event.destinationAddress(),
						event.zipCode(),
						event.phone(),
						false
				)
		);
	}

}
