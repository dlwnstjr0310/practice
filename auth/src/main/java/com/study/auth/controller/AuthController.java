package com.study.auth.controller;

import com.study.auth.controller.docs.AuthControllerDocs;
import com.study.auth.model.request.*;
import com.study.auth.model.response.MemberResponseDTO;
import com.study.auth.model.response.Response;
import com.study.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

	private final AuthService authService;

	@PostMapping("/join")
	public Response<Void> join(@Valid @RequestBody JoinDTO request) {

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
	public Response<MemberResponseDTO> verifyMail(HttpServletRequest request,
	                                              HttpServletResponse response,
	                                              @Valid @RequestBody VerifyDTO requestDTO) {

		return Response.<MemberResponseDTO>builder()
				.data(authService.verifyMail(request, response, requestDTO))
				.build();
	}

	@PostMapping("/login")
	public Response<MemberResponseDTO> login(HttpServletRequest request,
	                                         HttpServletResponse response,
	                                         @Valid @RequestBody LoginDTO requestDTO) {

		return Response.<MemberResponseDTO>builder()
				.data(authService.login(request, response, requestDTO))
				.build();
	}

	@DeleteMapping("/logout")
	public Response<Void> logout(HttpServletRequest request, @Valid @RequestBody LogoutDTO requestDTO) {

		authService.logout(request, requestDTO);

		return Response.<Void>builder()
				.build();
	}

	@PostMapping("/token")
	public Response<MemberResponseDTO> reissueAccessToken(HttpServletRequest request,
	                                                      HttpServletResponse response,
	                                                      @Valid @RequestBody ReissueAccessTokenDTO requestDTO) {

		return Response.<MemberResponseDTO>builder()
				.data(authService.reissueAccessToken(request, response, requestDTO))
				.build();
	}

	@PatchMapping("/modify-password")
	public Response<Void> modifyPassword(@Valid @RequestBody PwdModifyDTO request) {

		authService.modifyPassword(request);

		return Response.<Void>builder()
				.build();
	}
}
