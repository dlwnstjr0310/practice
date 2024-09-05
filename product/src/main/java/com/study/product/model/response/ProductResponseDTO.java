package com.study.product.model.response;

import com.study.product.domain.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductResponseDTO(

		@NotNull
		Long id,

		@NotBlank
		String name,

		@NotNull
		Integer price,

		@NotNull
		Integer stock,

		@NotNull
		Boolean isVisible
) {

	public static ProductResponseDTO of(Product product) {
		return new ProductResponseDTO(
				product.getId(),
				product.getName(),
				product.getPrice(),
				product.getStock(),
				product.getIsVisible()
		);
	}
}
