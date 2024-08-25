package com.study.member.service;

import com.study.member.client.OrderClient;
import com.study.member.client.ProductClient;
import com.study.member.domain.entity.Address;
import com.study.member.domain.entity.WishList;
import com.study.member.domain.entity.member.Member;
import com.study.member.domain.event.AddressEvent;
import com.study.member.exception.member.NotFoundMemberException;
import com.study.member.exception.member.NotFoundWishListException;
import com.study.member.exception.member.QuantityNotEnoughException;
import com.study.member.model.request.member.AddressRequestDTO;
import com.study.member.model.request.member.WishListRequestDTO;
import com.study.member.model.response.member.MemberInfoResponseDTO;
import com.study.member.model.response.member.MyPageResponseDTO;
import com.study.member.model.response.order.OrderResponseDTO;
import com.study.member.repository.AddressRepository;
import com.study.member.repository.MemberRepository;
import com.study.member.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final OrderClient orderClient;
	private final ProductClient productClient;
	private final MemberRepository memberRepository;
	private final WishListRepository wishListRepository;
	private final AddressRepository addressRepository;

	@Transactional(readOnly = true)
	public MyPageResponseDTO getMemberPage(Long id) {

		CompletableFuture<List<Address>> addressListFuture = CompletableFuture.supplyAsync(() ->
				addressRepository.findByMemberId(id)
		);

		CompletableFuture<List<WishList>> wishListFuture = CompletableFuture.supplyAsync(() ->
				wishListRepository.findAllByMemberId(id)
		);

		CompletableFuture<List<OrderResponseDTO>> orderListFuture = CompletableFuture.supplyAsync(() ->
				orderClient.getMemberOrderList(id)
		);


		return addressListFuture
				.thenCombine(wishListFuture, (addressList, wishList) ->
						MemberInfoResponseDTO.of(addressList.get(0).getMember(), addressList, wishList)
				)
				.thenCombine(orderListFuture, MyPageResponseDTO::of)
				.join();
	}

	@Transactional
	public void createWishList(Long id, WishListRequestDTO request) {

		Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);

		wishListRepository.save(request.toEntity(member.getId()));
	}

	@Transactional
	public Long modifyWishList(Long id, Integer quantity) {

		if (quantity < 1) {
			throw new QuantityNotEnoughException();
		}

		WishList wishList = wishListRepository.findById(id).orElseThrow(NotFoundWishListException::new);

		wishList.updateForQuantity(quantity);

		return wishListRepository.save(wishList).getId();
	}

	@Transactional
	public void deleteWishList(Long id) {
		wishListRepository.deleteById(id);
	}

	@Transactional
	public void createAddress(AddressRequestDTO request) {

		if (request.isDefault()) {
			List<Address> addressList = addressRepository.findByMemberId(request.memberId());

			addressList.forEach(Address::markAsFalse);

			addressList.add(request.toEntity());

			addressRepository.saveAll(addressList);
		} else {
			addressRepository.save(request.toEntity());
		}
	}

	@Transactional
	public void updateAddress(AddressEvent request) {

		addressRepository.save(Address.builder()
				.member(Member.builder().id(request.memberId()).build())
				.name(request.addressAlias())
				.address(request.destinationAddress())
				.zipCode(request.zipCode())
				.phone(request.phone())
				.build());
	}
}
