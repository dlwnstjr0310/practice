package com.study.product.model.response;

import java.util.List;

public record ProductPaginationResponseDTO(

		Integer totalElements,

		List<ProductResponseDTO> productList
) {

	public static ProductPaginationResponseDTO of(Integer totalElements, List<ProductResponseDTO> productList) {
		return new ProductPaginationResponseDTO(
				totalElements,
				productList
		);
	}
}
