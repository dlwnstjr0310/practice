package com.study.product.exception.product;

import com.study.product.exception.Error;
import com.study.product.exception.ProductException;
import org.springframework.http.HttpStatus;

public class NotCorrectCategoryException extends ProductException {
	public NotCorrectCategoryException() {
		super(Error.NOT_CORRECT_CATEGORY, HttpStatus.BAD_REQUEST);
	}
}
