package com.study.member.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.member.exception.Error;
import com.study.member.model.response.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

		if (userAgent == null || userAgent.isEmpty()) {
			createErrorResponse(response, Error.UNABLE_CONFIRM_USER_DEVICE, HttpServletResponse.SC_FORBIDDEN);
			response.flushBuffer();
			return;
		}

		filterChain.doFilter(request, response);
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
