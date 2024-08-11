package com.study.web.controller.docs;

import com.study.web.model.request.MemberRequest;
import com.study.web.model.response.LoginResponse;
import com.study.web.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Auth", description = "회원가입 및 로그인 등의 사용자 API")
public interface AuthControllerDocs {

	@Operation(summary = "회원가입", description = "신규 사용자 회원가입 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "회원가입 요청 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "409", description = """
					1. 이미 존재하는 Email 입니다.
					""", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/auth/join")
	Response<Void> join(MemberRequest.Join request);

	@Operation(summary = "이메일 인증번호 발송", description = "회원가입 진행중 등록한 이메일로 인증번호를 발송합니다.")
	@ApiResponse(responseCode = "200", description = "이메일 발송 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	@GetMapping("/auth/mail-certification/send")
	Response<Void> sendMail(String email);

	@Operation(summary = "이메일 인증번호 확인", description = "회원가입 진행중 등록한 이메일로 발송된 인증번호를 확인합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "이메일 인증 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "인증번호가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "408", description = "인증번호가 만료되었습니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/auth/mail-certification/verify")
	Response<LoginResponse.Info> verifyMail(MemberRequest.Verify request);
}
