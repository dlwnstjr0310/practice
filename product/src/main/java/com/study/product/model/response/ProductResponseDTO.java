package com.study.product.model.response;

import com.study.product.domain.entity.Product;

import java.util.List;

public record ProductResponseDTO(
		Long id,
		String name,
		Integer price,
		Integer stock,
		Boolean isVisible
) {

	public static List<ProductResponseDTO> of(List<Product> productList) {
		return productList.stream().map(ProductResponseDTO::of).toList();
	}

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
