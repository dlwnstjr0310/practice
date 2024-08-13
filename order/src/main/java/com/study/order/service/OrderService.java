package com.study.order.service;

import com.study.order.client.MemberClient;
import com.study.order.client.ProductClient;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.entity.order.Status;
import com.study.order.exception.order.AlreadyShippingException;
import com.study.order.exception.order.NotFoundOrderException;
import com.study.order.exception.order.ReturnPeriodPassedException;
import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.response.MemberResponseDTO;
import com.study.order.model.response.OrderResponseDTO;
import com.study.order.model.response.ProductResponseDTO;
import com.study.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductClient productClient;
	private final MemberClient memberClient;

	@Transactional
	public void createOrder(OrderRequestDTO request) {

		//todo: 배송지목록에 추가여부 확인
		if (request.isDefault()) {
			//todo: request 에 isDefault 면 member 업데이트 이벤트 발행
		}

		//todo: 추후 재고부족 에러 추가
		MemberResponseDTO memberInfo = memberClient.getMemberInfo(request.memberId());
		List<ProductResponseDTO> productList = productClient.getProductList(request.productIdList());

		orderRepository.save(
				Order.builder()
						.memberId(
								memberInfo.id()
						)
						.totalPrice(
								productList.stream()
										.mapToInt(ProductResponseDTO::price)
										.sum()
						)
						.destinationAddress(request.destinationAddress())
						.build()
		);
	}

	@Transactional(readOnly = true)
	public OrderResponseDTO getOrderList(Long id) {

		return OrderResponseDTO.of(
				orderRepository.findById(id)
						.orElseThrow(NotFoundOrderException::new)
		);
	}

	@Transactional
	public void modifyOrderStatus(List<Long> orderIdList, String status) {

		//todo: 주문 상태 변경 후 재고 복구 이벤트 발행
		List<Order> orderList = orderRepository.findAllById(orderIdList);

		if (status.equalsIgnoreCase("cancel")) {
			// 부분취소 안된다는 가정하에 진행
			for (Order order : orderList) {
				if (order.getStatus().equals(Status.ORDER_COMPLETED)) {
					order.setStatus(Status.CANCELED);
				} else {
					throw new AlreadyShippingException();
				}
			}
		} else if (status.equalsIgnoreCase("return")) {
			for (Order order : orderList) {
				if (order.getStatus().equals(Status.SHIPPING_COMPLETED)) {
					order.setStatus(Status.RETURN_PENDING);
				} else {
					throw new ReturnPeriodPassedException();
				}
			}
		}
	}

}
