package com.study.product.model.response;

import java.util.List;

public record ProductSearchResultDTO(

		Integer totalElements,

		List<ProductResponseDTO> productList
) {

	public static ProductSearchResultDTO of(Integer totalElements, List<ProductResponseDTO> productList) {
		return new ProductSearchResultDTO(
				totalElements,
				productList
		);
	}
}
