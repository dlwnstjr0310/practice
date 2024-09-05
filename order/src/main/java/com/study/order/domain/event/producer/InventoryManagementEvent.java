package com.study.order.domain.event.producer;

import java.util.Map;

public record InventoryManagementEvent(

		Map<Long, Integer> productQuantityMap,

		Boolean isIncrease

) {
}
