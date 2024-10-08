package com.study.product.model.request;

import com.study.product.domain.entity.Category;
import com.study.product.domain.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequestDTO(

		@NotBlank
		String name,

		@NotNull
		Integer price,

		@NotNull
		Integer stock,

		@NotNull
		String category,

		@NotNull
		Boolean isVisible
) {

	public Product toEntity() {
		return Product.builder()
				.name(name)
				.price(price)
				.stock(stock)
				.category(Category.valueOf(category))
				.isVisible(isVisible)
				.build();
	}

}
