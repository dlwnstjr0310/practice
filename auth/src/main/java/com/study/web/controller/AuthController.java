package com.study.web.controller;

import com.study.web.controller.docs.AuthControllerDocs;
import com.study.web.model.request.MemberRequest;
import com.study.web.model.response.MemberResponse;
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
	public Response<Void> join(@Valid @RequestBody MemberRequest.Join request) {

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
	public Response<MemberResponse.Info> verifyMail(@Valid @RequestBody MemberRequest.Verify request) {

		return Response.<MemberResponse.Info>builder()
				.data(authService.verifyMail(request))
				.build();
	}

	@PostMapping("/login")
	public Response<MemberResponse.Info> login(@Valid @RequestBody MemberRequest.Login request) {

		return Response.<MemberResponse.Info>builder()
				.data(authService.login(request))
				.build();
	}

	@PostMapping("/logout")
	public Response<Void> logout(@Valid @RequestBody MemberRequest.Logout request) {

		authService.logout(request);

		return Response.<Void>builder()
				.build();
	}
}
