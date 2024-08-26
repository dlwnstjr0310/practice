package com.study.product.service;

import com.study.product.domain.entity.Product;
import com.study.product.domain.event.consumer.InventoryManagementEvent;
import com.study.product.exception.product.NotFoundProductException;
import com.study.product.model.request.DiscountSaleProductRequestDTO;
import com.study.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductAsyncService {

	private static final String PRODUCT_KEY_PREFIX = "product:";

	private final RedisService redisService;
	private final ProductRepository productRepository;

	@Transactional
	public void setUpDiscountSaleProduct(DiscountSaleProductRequestDTO request) {

		Product product = productRepository.findById(request.id())
				.orElseThrow(NotFoundProductException::new);

		int discountPrice = product.getPrice() - (product.getPrice() * request.discountRate() / 100);

		redisService.storeInRedis(
				PRODUCT_KEY_PREFIX + request.id(),
				request.quantity().toString() + ":" + discountPrice
		);

	}

	@Transactional
	public void managementStock(InventoryManagementEvent event) {

		Map<Long, Integer> productQuantityMap = event.productQuantityMap();

		productQuantityMap.forEach((key, value) -> productRepository.findById(key)
				.ifPresent(product -> {
					if (event.isIncrease()) {
						product.setCurrentStock(product.getStock() + value);
					} else {
						product.setCurrentStock(product.getStock() - value);
					}
					productRepository.save(product);
				}));
	}
}
