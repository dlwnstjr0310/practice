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
import com.study.member.exception.product.IsNotSaleProductException;
import com.study.member.exception.product.NotFoundProductException;
import com.study.member.model.request.AddressRequestDTO;
import com.study.member.model.request.WishListRequestDTO;
import com.study.member.model.response.Response;
import com.study.member.model.response.member.MemberResponseDTO;
import com.study.member.model.response.member.WishListResponseDTO;
import com.study.member.model.response.order.OrderResponseDTO;
import com.study.member.model.response.product.ProductResponseDTO;
import com.study.member.repository.AddressRepository;
import com.study.member.repository.MemberRepository;
import com.study.member.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.study.member.exception.Error.IS_NOT_SALE_PRODUCT;
import static com.study.member.exception.Error.NOT_FOUND_PRODUCT;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final OrderClient orderClient;
	private final ProductClient productClient;
	private final MemberRepository memberRepository;
	private final WishListRepository wishListRepository;
	private final AddressRepository addressRepository;

	@Transactional(readOnly = true)
	public MemberResponseDTO getMemberPage(Long id) {

		// nullable
		List<WishList> wishList = wishListRepository.findAllByMemberId(id);

		List<OrderResponseDTO> memberOrderList = orderClient.getMemberOrderList(id).data();

		return MemberResponseDTO.of(
				memberOrderList,
				WishListResponseDTO.of(wishList)
		);
	}

	@Transactional
	public void createWishList(Long id, WishListRequestDTO request) {

		Response<ProductResponseDTO> productDetail = productClient.getProductDetail(request.productId());

		if (productDetail.code().equals(NOT_FOUND_PRODUCT.getCode())) {
			throw new NotFoundProductException();
		}
		if (productDetail.code().equals(IS_NOT_SALE_PRODUCT.getCode())) {
			throw new IsNotSaleProductException();
		}

		Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberException::new);
		ProductResponseDTO product = productDetail.data();

		wishListRepository.save(
				request.toEntity(product.price(), member.getId())
		);

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
