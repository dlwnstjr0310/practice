package com.study.product.repository.custom.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.study.product.model.request.SearchConditionDTO;
import com.study.product.model.response.ProductResponseDTO;
import com.study.product.repository.custom.ProductRepositoryCustom;
import com.study.product.util.QueryDslUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.study.product.domain.entity.QProduct.product;
import static com.study.product.util.QueryDslUtil.eqCategory;
import static com.study.product.util.QueryDslUtil.toContainsExpression;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ProductResponseDTO> search(Pageable pageable, SearchConditionDTO searchCondition) {

		List<OrderSpecifier> orders = QueryDslUtil.getAllOrderSpecifiers(pageable);

		return queryFactory.select(
						Projections.bean(ProductResponseDTO.class,
								product.id.as("id"),
								product.name.as("name"),
								product.price.as("price"),
								product.stock.as("stock"),
								product.isVisible.as("isVisible")
						))
				.from(product)
				.where(
						eqCategory(searchCondition.category()),
						toContainsExpression(searchCondition),
						product.isVisible.isTrue()
				)
				.orderBy(orders.toArray(OrderSpecifier[]::new))
				.fetch();
	}

}
