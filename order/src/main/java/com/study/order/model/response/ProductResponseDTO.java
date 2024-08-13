package com.study.order.model.response;

public record ProductResponseDTO(
		Long id,
		String name,
		Integer price,
		Integer stock,
		Boolean isVisible
) {
}
