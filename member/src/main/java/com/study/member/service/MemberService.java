package com.study.member.service;

import com.study.member.domain.entity.member.Member;
import com.study.member.exception.member.NotFoundMemberException;
import com.study.member.model.response.MemberResponseDTO;
import com.study.member.repository.AddressRepository;
import com.study.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;

	@Transactional
	public MemberResponseDTO getMemberInfo(Long id) {

		Member member = memberRepository.findById(id)
				.orElseThrow(NotFoundMemberException::new);
		return MemberResponseDTO.of(
				member
		);
	}
}
