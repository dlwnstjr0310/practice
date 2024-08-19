package com.study.member.service;

import com.study.member.domain.event.DefaultAddressUpdateEvent;
import com.study.member.model.request.AddressRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberEventListener {

	private final MemberService memberService;

	@KafkaListener(topics = "default-address-update", groupId = "member-group")
	public void handleAddressUpdateEvent(DefaultAddressUpdateEvent event) {

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

	@KafkaListener(topics = "store-address", groupId = "member-group")
	public void handlerAddressStoreEvent(DefaultAddressUpdateEvent event) {

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
