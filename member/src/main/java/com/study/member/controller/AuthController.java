package com.study.member.controller;

import com.study.member.controller.docs.AuthControllerDocs;
import com.study.member.model.request.auth.*;
import com.study.member.model.response.Response;
import com.study.member.model.response.member.TokenResponseDTO;
import com.study.member.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

	private final AuthService authService;

	@PostMapping("/join")
	public Response<Void> join(@Valid @RequestBody JoinRequestDTO request) {

		authService.join(request);

		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}

	@PostMapping("/mail-certification/send")
	public Response<Void> sendMail(@Valid @RequestParam String email) {

		authService.sendMail(email);

		return Response.<Void>builder()
				.build();
	}

	@PostMapping("/mail-certification/verify")
	public Response<TokenResponseDTO> verifyMail(HttpServletRequest request,
	                                             HttpServletResponse response,
	                                             @Valid @RequestBody JoinVerifyRequestDTO requestDTO) {

		return Response.<TokenResponseDTO>builder()
				.data(authService.verifyMail(request, response, requestDTO))
				.build();
	}

	@PostMapping("/login")
	public Response<TokenResponseDTO> login(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        @Valid @RequestBody LoginRequestDTO requestDTO) {

		return Response.<TokenResponseDTO>builder()
				.data(authService.login(request, response, requestDTO))
				.build();
	}

	@DeleteMapping("/logout")
	public Response<Void> logout(HttpServletRequest request, @Valid @RequestBody LogoutRequestDTO requestDTO) {

		authService.logout(request, requestDTO);

		return Response.<Void>builder()
				.build();
	}

	@PostMapping("/token")
	public Response<TokenResponseDTO> reissueAccessToken(HttpServletRequest request,
	                                                     HttpServletResponse response,
	                                                     @Valid @RequestBody ReissueTokenRequestDTO requestDTO) {

		return Response.<TokenResponseDTO>builder()
				.data(authService.reissueAccessToken(request, response, requestDTO))
				.build();
	}

	@PatchMapping("/modify-password")
	public Response<Void> modifyPassword(@Valid @RequestBody PwdModifyRequestDTO request) {

		authService.modifyPassword(request);

		return Response.<Void>builder()
				.build();
	}
}
