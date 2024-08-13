package com.study.product.service;

import com.study.product.domain.entity.Product;
import com.study.product.exception.product.IsNotSaleProductException;
import com.study.product.exception.product.NotFoundProductException;
import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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
	public ProductResponseDTO modifyProduct(Long id, ProductRequestDTO request) {

		Product product = productRepository.findById(id)
				.orElseThrow(NotFoundProductException::new);

		product.modifyForProductField(
				request.name(),
				request.price(),
				request.stock(),
				request.isVisible()
		);

		productRepository.save(product);

		return new ProductResponseDTO(
				product.getId(),
				product.getName(),
				product.getPrice(),
				product.getStock(),
				product.getIsVisible()
		);
	}

	@Transactional(readOnly = true)
	public List<ProductResponseDTO> getCurrentSaleProductList() {

		return ProductResponseDTO.of(productRepository.findAllByIsVisibleTrue());
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

	@Transactional
	public List<ProductResponseDTO> getProductList(List<Long> idList) {

		//todo: 여기 대체 왜 post 로 쳐들어오는지 이해가안됨; 무조건밝혀내
		System.out.println("상품목록 가져ㅑ오기 실행 : " + idList);
		return productRepository.findAllById(idList).stream()
				.map(product -> {
					if (!product.getIsVisible()) {
						throw new IsNotSaleProductException();
					}
					return ProductResponseDTO.of(product);
				}).toList();
	}
}
