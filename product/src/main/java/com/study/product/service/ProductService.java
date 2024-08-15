package com.study.product.service;

import com.study.product.domain.entity.Product;
import com.study.product.exception.product.IsNotSaleProductException;
import com.study.product.exception.product.NotFoundProductException;
import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductPaginationResponseDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public Long createProduct(ProductRequestDTO request) {

		return productRepository.save(request.toEntity()).getId();
	}

	@Transactional
	public Long modifyProduct(Long id, ProductRequestDTO request) {

		Product product = productRepository.findById(id)
				.orElseThrow(NotFoundProductException::new);

		product.modifyForProductField(
				request.name(),
				request.price(),
				request.stock(),
				request.isVisible()
		);

		productRepository.save(product);

		return product.getId();
	}

	@Transactional(readOnly = true)
	public ProductPaginationResponseDTO getCurrentSaleProductList(Pageable pageable, SearchConditionDTO searchCondition) {

		List<ProductResponseDTO> search = productRepository.search(pageable, searchCondition);
		return ProductPaginationResponseDTO.of(
				search.size(),
				search
		);
	}

	@Transactional(readOnly = true)
	public ProductResponseDTO getProductDetail(Long id) {

		Product product = productRepository.findById(id)
				.orElseThrow(NotFoundProductException::new);

		if (!product.getIsVisible()) {
			throw new IsNotSaleProductException();
		}

		return ProductResponseDTO.of(product);
	}
}
