package com.study.member.service;

import com.study.member.exception.member.NotFoundMemberException;
import com.study.member.model.response.MemberInfoResponseDTO;
import com.study.member.repository.AddressRepository;
import com.study.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberOrderService {

	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;

	@Transactional
	public MemberInfoResponseDTO getMemberInfo(Long id) {

		return MemberInfoResponseDTO.of(
				memberRepository.findById(id)
						.orElseThrow(NotFoundMemberException::new)
		);
	}
}
