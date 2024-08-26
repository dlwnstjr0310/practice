package com.study.product.domain.event.consumer;

import java.util.Map;

public record InventoryManagementEvent(

		Map<Long, Integer> productQuantityMap,

		Boolean isIncrease

) {
}
