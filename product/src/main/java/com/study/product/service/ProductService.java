package com.study.product.service;

import com.study.product.domain.entity.Product;
import com.study.product.domain.event.consumer.InventoryManagementEvent;
import com.study.product.exception.order.OutOfStockException;
import com.study.product.exception.product.IsNotSaleProductException;
import com.study.product.exception.product.NotFoundProductException;
import com.study.product.model.request.DiscountSaleProductRequestDTO;
import com.study.product.model.request.ProductOrderRequestDTO;
import com.study.product.model.request.ProductRequestDTO;
import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductOrderResponseDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.model.response.ProductSearchResultDTO;
import com.study.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

	private static final String PRODUCT_KEY_PREFIX = "product:";

	private final RedisService redisService;
	private final ProductRepository productRepository;

	@Transactional
	public Long createProduct(ProductRequestDTO request) {

		return productRepository.save(request.toEntity()).getId();
	}

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
	public ProductSearchResultDTO getCurrentSaleProductList(Pageable pageable, SearchConditionDTO searchCondition) {

		List<ProductResponseDTO> search = productRepository.search(pageable, searchCondition);
		return ProductSearchResultDTO.of(
				search.size(),
				search
		);
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
