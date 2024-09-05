package com.study.product.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.study.product.domain.entity.Category;
import com.study.product.exception.product.NotCorrectCategoryException;
import com.study.product.model.request.SearchConditionDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.study.product.domain.entity.QProduct.product;

public class QueryDslUtil {

	public static <T> OrderSpecifier<?> getSortedColumn(Order order, Path<T> parent, String fieldName) {
		Path<T> fieldPath = Expressions.path(parent.getType(), parent, fieldName);
		return new OrderSpecifier(order, fieldPath);
	}

	public static List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

		List<OrderSpecifier> orders = new ArrayList<>();

		if (!pageable.getSort().isEmpty()) {

			for (Sort.Order order : pageable.getSort()) {

				Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

				switch (order.getProperty()) {
					case "name" -> orders.add(getSortedColumn(direction, product, "name"));
					case "price" -> orders.add(getSortedColumn(direction, product, "price"));
					case "category" -> orders.add(getSortedColumn(direction, product, "category"));
					default -> orders.add(getSortedColumn(direction, product, "id"));
				}
			}
		}
		return orders;
	}

	public static BooleanExpression toContainsExpression(SearchConditionDTO searchCondition) {
		if (searchCondition.keyword() == null) {
			return null;
		}

		return switch (searchCondition.searchValue()) {
			case "name" -> product.name.contains(searchCondition.keyword());
			case "price" -> product.price.loe(Integer.parseInt(searchCondition.keyword()));
			default -> throw new NotCorrectCategoryException();
		};
	}

	public static BooleanExpression eqCategory(String searchValue) {
		if (searchValue == null || searchValue.isEmpty()) {
			return null;
		}

		Category category = switch (searchValue) {
			case "A" -> Category.A;
			case "B" -> Category.B;
			case "C" -> Category.C;
			case "D" -> Category.D;
			case "E" -> Category.E;
			default -> throw new NotCorrectCategoryException();
		};

		return product.category.eq(category);
	}
}
