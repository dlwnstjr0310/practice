package com.study.product.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {

	A("A 카테고리", 1),
	B("B 카테고리", 2),
	C("C 카테고리", 3),
	D("D 카테고리", 4),
	E("E 카테고리", 5);
	
	private final String description;
	private final Integer code;
}
