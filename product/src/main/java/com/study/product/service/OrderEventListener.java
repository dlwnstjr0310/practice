package com.study.product.service;

import com.study.product.domain.event.consumer.InventoryManagementEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventListener {

	private static final String INVENTORY_MANAGEMENT_EVENT = "inventory-management-event";

	private final ProductAsyncService productService;

	@KafkaListener(topics = INVENTORY_MANAGEMENT_EVENT,
			properties = "spring.json.value.default.type=com.study.product.domain.event.consumer.InventoryManagementEvent"
	)
	public void handleOrderSuccessEvent(InventoryManagementEvent event) {
		productService.managementStock(event);
	}

}
