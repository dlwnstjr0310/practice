package com.study.product.service;

import com.study.product.domain.entity.Product;
import com.study.product.domain.event.consumer.OrderSuccessEvent;
import com.study.product.exception.product.NotFoundProductException;
import com.study.product.model.request.DiscountSaleProductRequestDTO;
import com.study.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public void decreaseStock(OrderSuccessEvent event) {

		productRepository.findById(event.productId())
				.ifPresent(product -> {
					product.setCurrentStock(product.getStock() - event.quantity());
					productRepository.save(product);
				});
	}
}
