package com.study.order.service;

import com.study.order.client.MemberClient;
import com.study.order.client.ProductClient;
import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.entity.order.Order;
import com.study.order.domain.entity.order.Status;
import com.study.order.domain.event.producer.AddressEvent;
import com.study.order.exception.order.AlreadyShippingException;
import com.study.order.exception.order.NotFoundOrderException;
import com.study.order.exception.order.OutOfStockException;
import com.study.order.exception.order.ReturnPeriodPassedException;
import com.study.order.model.request.OrderRequestDTO;
import com.study.order.model.request.ProductOrderRequestDTO;
import com.study.order.model.response.order.OrderResponseDTO;
import com.study.order.model.response.order.ProductOrderResponseDTO;
import com.study.order.repository.OrderDetailRepository;
import com.study.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class OrderService {

	private final MemberClient memberClient;
	private final ProductClient productClient;
	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;

	@Transactional
	public synchronized void createOrder(OrderRequestDTO request) {

		List<ProductOrderResponseDTO> productOrderList = productClient.getProductOrderList(
				request.productList().stream()
						.map(ProductOrderRequestDTO::productId)
						.toList()
		);

		Map<Long, ProductOrderResponseDTO> productMap = productOrderList.stream()
				.collect(Collectors.toMap(ProductOrderResponseDTO::productId, Function.identity()));

		request.productList().forEach(product -> {
			ProductOrderResponseDTO productResponseDTO = productMap.get(product.productId());

			if (productResponseDTO.quantity() - product.quantity() < 0) {
				throw new OutOfStockException();
			}
		});

		List<ProductOrderResponseDTO> productList = productClient.modifyProductStock(request.productList());

		AtomicInteger totalPrice = new AtomicInteger();
		Set<OrderDetail> orderDetailList = new LinkedHashSet<>();

		Order order = Order.builder()
				.memberId(request.memberId())
				.destinationAddress(request.destinationAddress())
				.build();

		productList.forEach(product -> {

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

			memberClient.updateAddress(
					new AddressEvent(
							request.memberId(),
							request.addressAlias(),
							request.destinationAddress(),
							request.zipCode(),
							request.phone()
					)
			);
		}
	}

	@Transactional
	public List<OrderDetail> modifyOrderStatus(Long id, String status) {

		List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(id);
		Order order = orderDetailList.get(0).getOrder();

		if (status.equalsIgnoreCase("cancel")) {
			// 부분취소 안된다는 가정하에 진행
			if (order.getStatus().equals(ORDER_COMPLETED)) {
				order.updateStatus(ORDER_CANCELED);

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

		return orderDetailList;
	}

	@Transactional
	public void store(OrderDetail orderDetail, Status status) {

		Order order = orderDetail.getOrder();

		order.updateStatus(status);

		orderRepository.save(order);
		orderDetailRepository.save(orderDetail);
	}

	@Transactional
	public void store(Long id, Status status) {

		Order order = orderRepository.findById(id).orElseThrow(NotFoundOrderException::new);

		order.updateStatus(status);

		orderRepository.save(order);
	}

	@Transactional(readOnly = true)
	public List<OrderResponseDTO> getMemberOrderList(Long id) {

		return orderDetailRepository.findOrderListAndOrderDetailListByMemberId(id);
	}

	@Transactional(readOnly = true)
	public OrderResponseDTO getOrderDetailList(Long id) {

		List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrderId(id);
		Order order = orderDetailList.get(0).getOrder();

		return OrderResponseDTO.of(
				order,
				orderDetailList
		);
	}
}
