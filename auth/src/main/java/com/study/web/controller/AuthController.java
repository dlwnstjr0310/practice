package com.study.web.controller;

import com.study.web.controller.docs.AuthControllerDocs;
import com.study.web.model.request.*;
import com.study.web.model.response.MemberResponseDTO;
import com.study.web.model.response.Response;
import com.study.web.service.AuthService;
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
	public Response<MemberResponseDTO> verifyMail(@Valid @RequestBody VerifyDTO request) {

		return Response.<MemberResponseDTO>builder()
				.data(authService.verifyMail(request))
				.build();
	}

	@PostMapping("/login")
	public Response<MemberResponseDTO> login(@Valid @RequestBody LoginDTO request) {

		return Response.<MemberResponseDTO>builder()
				.data(authService.login(request))
				.build();
	}

	@DeleteMapping("/logout")
	public Response<Void> logout(@Valid @RequestBody LogoutDTO request) {

		authService.logout(request);

		return Response.<Void>builder()
				.build();
	}

	@PostMapping("/token")
	public Response<MemberResponseDTO> reissueAccessToken(@Valid @RequestBody ReissueAccessTokenDTO request) {

		return Response.<MemberResponseDTO>builder()
				.data(authService.reissueAccessToken(request))
				.build();
	}

	@PatchMapping("/modify-password")
	public Response<Void> modifyPassword(@Valid @RequestBody LoginDTO request) {

		authService.modifyPassword(request);

		return Response.<Void>builder()
				.build();
	}
}
