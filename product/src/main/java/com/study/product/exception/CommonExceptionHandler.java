package com.study.product.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(basePackages = {"com.study.web.controller"})
public class CommonExceptionHandler {

//	@ExceptionHandler(LoginException.class)
//	public Response<Void> loginExceptionHandler(LoginException e) {
//
//		java.lang.Error authError = e.getError();
//
//		log.warn("login exception info : {} ", e.getMessage());
//
//		return Response.<Void>builder()
//				.code(authError.getCode())
//				.message(authError.getMessage())
//				.build();
//	}
//
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public Response<Void> validExceptionHandler(MethodArgumentNotValidException e) {
//
//		BindingResult bindingResult = e.getBindingResult();
//		String errorMessage;
//
//		try {
//			errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
//		} catch (NullPointerException exception) {
//			errorMessage = Objects.requireNonNull(bindingResult.getGlobalError()).getDefaultMessage();
//		}
//
//		return Response.<Void>builder()
//				.code(HttpStatus.BAD_REQUEST.value())
//				.message(String.valueOf(errorMessage))
//				.build();
//	}
//
//	@ExceptionHandler(Exception.class)
//	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//	public Response<Void> exceptionHandler(Exception e) {
//		return Response.<Void>builder()
//				.code(java.lang.Error.INTERNAL_SERVER_ERROR.getCode())
//				.message(java.lang.Error.INTERNAL_SERVER_ERROR.getMessage())
//				.build();
//	}
//
//	private Response<Void> createResponse(java.lang.Error authError) {
//		return createResponse(authError.getCode(), authError.getMessage());
//	}
//
//	private Response<Void> createResponse(Integer code, String message) {
//		return Response.<Void>builder()
//				.code(code)
//				.message(message)
//				.build();
//	}

}
