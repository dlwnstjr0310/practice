package com.study.auth.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.auth.exception.Error;
import com.study.auth.model.response.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class HeaderFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String userAgent = request.getHeader("User-Agent");

		if (userAgent == null || userAgent.isEmpty()) {
			createError(response, Error.UNABLE_CONFIRM_USER_DEVICE);
			response.flushBuffer();
			return;
		}

		filterChain.doFilter(request, response);
	}

	private void createError(HttpServletResponse response, Error error) {

		ObjectMapper objectMapper = new ObjectMapper();

		response.setStatus(HttpStatus.SC_UNAUTHORIZED);
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
