package com.study.product.service;

import com.study.product.domain.entity.Product;
import com.study.product.exception.order.OutOfStockException;
import com.study.product.exception.product.IsNotSaleProductException;
import com.study.product.model.request.ProductOrderRequestDTO;
import com.study.product.model.response.ProductOrderResponseDTO;
import com.study.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductOrderService {

	private final ProductRepository productRepository;

	@Transactional
	public List<ProductOrderResponseDTO> modifyProductStock(List<ProductOrderRequestDTO> request) {

		Map<Long, Product> map = productRepository.findAllById(
						request.stream()
								.map(ProductOrderRequestDTO::productId)
								.toList()
				).stream()
				.collect(Collectors.toMap(Product::getId, Function.identity()));

		List<ProductOrderResponseDTO> orderList = request.stream()
				.map(target -> {
					Product product = map.get(target.productId());

					int stock = product.getStock() - target.quantity();

					if (stock < 0) {
						throw new OutOfStockException();
					}

					product.setCurrentStock(stock);

					return ProductOrderResponseDTO.of(
							product.getId(),
							target.quantity(),
							product.getPrice()
					);
				}).toList();

		productRepository.saveAll(map.values());

		return orderList;
	}

	@Transactional(readOnly = true)
	public List<ProductOrderResponseDTO> getProductOrderList(List<Long> productIdList) {

		return productRepository.findAllById(productIdList).stream()
				.map(product -> {

					if (!product.getIsVisible()) {
						throw new IsNotSaleProductException();
					}

					return ProductOrderResponseDTO.of(
							product.getId(),
							product.getStock(),
							product.getPrice()
					);
				})
				.toList();
	}
}
