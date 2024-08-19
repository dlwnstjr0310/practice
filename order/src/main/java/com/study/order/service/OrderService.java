package com.study.order.service;

import com.study.order.client.MemberClient;
import com.study.order.client.ProductClient;
import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.event.DefaultAddressUpdateEvent;
import com.study.order.exception.member.NotFoundMemberException;
import com.study.order.exception.order.AlreadyShippingException;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.exception.order.ReturnPeriodPassedException;
import com.study.order.exception.product.IsNotSaleProductException;
import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.Response;
import com.study.order.model.response.member.MemberResponseDTO;
import com.study.order.model.response.order.OrderResponseDTO;
import com.study.order.model.response.order.ProductOrderResponseDTO;
import com.study.order.repository.OrderDetailRepository;
import com.study.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.study.order.domain.entity.order.Status.*;
import static com.study.order.exception.Error.IS_NOT_SALE_PRODUCT;
import static com.study.order.exception.Error.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final MemberClient memberClient;
	private final ProductClient productClient;
	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final KafkaTemplate<String, DefaultAddressUpdateEvent> kafkaTemplate;

	@Transactional
	public void createOrder(OrderRequestDTO request) {

		Response<MemberResponseDTO> memberInfo = memberClient.getMemberInfo(request.memberId());

		if (memberInfo.code().equals(NOT_FOUND_MEMBER.getCode())) {
			throw new NotFoundMemberException();
		}

		Response<List<ProductOrderResponseDTO>> productOrderList = productClient.getProductOrderList(
				request.productList().stream()
						.map(ProductOrderRequestDTO::productId)
						.toList()
		);

		if (productOrderList.code().equals(IS_NOT_SALE_PRODUCT.getCode())) {
			throw new IsNotSaleProductException();
		}

		Map<Long, ProductOrderResponseDTO> productMap = productOrderList.data().stream()
				.collect(Collectors.toMap(ProductOrderResponseDTO::productId, Function.identity()));

		request.productList().forEach(product -> {
			ProductOrderResponseDTO productResponseDTO = productMap.get(product.productId());

			if (productResponseDTO.quantity() - product.quantity() < 0) {
				throw new OutOfStockException();
			}
		});

		Response<List<ProductOrderResponseDTO>> productList = productClient.modifyProductStock(request.productList());

		AtomicInteger totalPrice = new AtomicInteger();
		Set<OrderDetail> orderDetailList = new LinkedHashSet<>();

		Order order = Order.builder()
				.memberId(memberInfo.data().id())
				.destinationAddress(request.destinationAddress())
				.build();

		productList.data().forEach(product -> {

			totalPrice.addAndGet(product.quantity() * product.piecePrice());

			orderDetailList.add(
					OrderDetail.builder()
							.order(order)
							.productId(product.productId())
							.quantity(product.quantity())
							.price(product.piecePrice())
							.build()
			);
		});

		order.updateTotalPrice(totalPrice.get());

		orderRepository.save(order);
		orderDetailRepository.saveAll(orderDetailList);

		if (request.isStoreInAddress()) {

			if (request.isDefault()) {
				kafkaTemplate.send(
						"default-address-update",
						new DefaultAddressUpdateEvent(
								request.memberId(),
								request.addressAlias(),
								request.destinationAddress(),
								request.zipCode(),
								request.phone()
						));
			} else {
				kafkaTemplate.send(
						"store-address",
						new DefaultAddressUpdateEvent(
								request.memberId(),
								request.addressAlias(),
								request.destinationAddress(),
								request.zipCode(),
								request.phone()
						)
				);
			}
		}
	}

	@Transactional(readOnly = true)
	public OrderResponseDTO getOrderList(Long id) {

		List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(id);
		Order order = orderDetailList.get(0).getOrder();

		return OrderResponseDTO.of(
				order,
				orderDetailList
		);
	}

	@Transactional
	public void modifyOrderStatus(Long id, String status) {

		//todo: 주문 상태 변경 후 재고 복구 이벤트 발행
		List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(id);
		Order order = orderDetailList.get(0).getOrder();

		if (status.equalsIgnoreCase("cancel")) {
			// 부분취소 안된다는 가정하에 진행
			if (order.getStatus().equals(ORDER_COMPLETED)) {
				order.updateStatus(CANCELED);

				orderDetailList.forEach(OrderDetail::markAsDelete);
			} else {
				throw new AlreadyShippingException();
			}
		} else if (status.equalsIgnoreCase("return")) {

			if (order.getStatus().equals(SHIPPING_COMPLETED)) {
				order.updateStatus(RETURN_PENDING);

				orderDetailList.forEach(OrderDetail::markAsDelete);
			} else {
				throw new ReturnPeriodPassedException();
			}
		}

		orderRepository.save(order);
		orderDetailRepository.saveAll(orderDetailList);
	}

}
