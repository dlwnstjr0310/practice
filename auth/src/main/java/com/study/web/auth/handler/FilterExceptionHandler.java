package com.study.web.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.web.exception.Error;
import com.study.web.exception.login.AccountNotEnabledException;
import com.study.web.exception.login.NotFoundMemberException;
import com.study.web.exception.token.ExpiredTokenException;
import com.study.web.exception.token.InvalidTokenException;
import com.study.web.model.response.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class FilterExceptionHandler extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (ExpiredTokenException e) {
			createErrorResponse(response, Error.EXPIRED_TOKEN, HttpServletResponse.SC_FORBIDDEN);
		} catch (InvalidTokenException e) {
			createErrorResponse(response, Error.INVALID_TOKEN, HttpServletResponse.SC_FORBIDDEN);
		} catch (AccountNotEnabledException e) {
			createErrorResponse(response, Error.ACCOUNT_NOT_ENABLED, HttpServletResponse.SC_FORBIDDEN);
		} catch (NotFoundMemberException e) {
			createErrorResponse(response, Error.UNAUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	private void createErrorResponse(HttpServletResponse response, Error error, int code) {

		ObjectMapper objectMapper = new ObjectMapper();

		response.setStatus(code);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		Response<Void> errorResponse = Response.<Void>builder()
				.code(error.getCode())
				.message(error.getMessage())
				.build();

		try {
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
