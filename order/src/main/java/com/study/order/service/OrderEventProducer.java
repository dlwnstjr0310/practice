package com.study.order.service;

import com.study.order.domain.entity.OrderDetail;
import com.study.order.domain.event.consumer.PaymentResultEvent;
import com.study.order.domain.event.producer.InventoryManagementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

	private static final String INVENTORY_MANAGEMENT_EVENT = "inventory-management-event";

	private final OrderService orderService;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void handleInventoryManagementEvent(PaymentResultEvent event) {

		Map<Long, Integer> productQuantityMap = new HashMap<>();

		productQuantityMap.put(event.productId(), event.quantity());

		sendEvent(INVENTORY_MANAGEMENT_EVENT,
				new InventoryManagementEvent(
						productQuantityMap,
						false
				)
		);
	}

	public void modifyOrderStatus(Long orderId, String status) {

		handleInventoryManagementEvent(orderService.modifyOrderStatus(orderId, status));
	}

	private void handleInventoryManagementEvent(List<OrderDetail> orderDetailList) {

		Map<Long, Integer> productQuantityMap = new HashMap<>();

		orderDetailList.forEach(orderDetail ->
				productQuantityMap.put(orderDetail.getProductId(), orderDetail.getQuantity())
		);

		sendEvent(INVENTORY_MANAGEMENT_EVENT,
				new InventoryManagementEvent(
						productQuantityMap,
						true
				)
		);
	}

	public void sendEvent(String topic, Object event) {
		kafkaTemplate.send(topic, event);
	}

}
