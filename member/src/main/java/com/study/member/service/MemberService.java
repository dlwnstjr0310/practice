package com.study.member.service;

import com.study.member.client.OrderClient;
import com.study.member.domain.entity.WishList;
import com.study.member.model.response.*;
import com.study.member.repository.MemberRepository;
import com.study.member.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final OrderClient orderClient;
	private final MemberRepository memberRepository;
	private final WishListRepository wishListRepository;

	@Transactional(readOnly = true)
	public MemberResponseDTO getMemberPage(Long id) {

		// nullable
		List<WishList> wishList = wishListRepository.findAllByMemberId(id);

		Map<OrderMemberResponseDTO, List<OrderDetailResponseDTO>> memberOrderList =
				orderClient.getMemberOrderList(id).data();

		List<OrderResponseDTO> orderList = new ArrayList<>();

		memberOrderList.forEach((k, v) -> orderList.add(
				new OrderResponseDTO(
						k.id(),
						k.status(),
						k.totalPrice(),
						k.destinationAddress(),
						v)
		));

		return MemberResponseDTO.of(
				orderList,
				WishListResponseDTO.of(wishList)
		);
	}
}
