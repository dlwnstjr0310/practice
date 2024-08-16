package com.study.auth.controller.docs;

import com.study.auth.model.request.*;
import com.study.auth.model.response.MemberResponseDTO;
import com.study.auth.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "회원가입 및 로그인 등의 사용자 API")
public interface AuthControllerDocs {

	@Operation(summary = "회원가입", description = "신규 사용자 회원가입 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "회원가입 요청 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "409", description = """
					1. 이미 존재하는 Email 입니다.
					""", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/auth/join")
	Response<Void> join(JoinDTO request);

	@Operation(summary = "이메일 인증번호 발송", description = "회원가입 진행중 등록한 이메일로 인증번호를 발송합니다.")
	@ApiResponse(responseCode = "200", description = "이메일 발송 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	@PostMapping("/auth/mail-certification/send")
	Response<Void> sendMail(String email);

	@Operation(summary = "이메일 인증번호 확인", description = "회원가입 진행중 등록한 이메일로 발송된 인증번호를 확인합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "이메일 인증 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "인증번호가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "408", description = "인증번호가 만료되었습니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/auth/mail-certification/verify")
	Response<MemberResponseDTO> verifyMail(HttpServletRequest request, HttpServletResponse response, VerifyDTO requestDTO);

	@Operation(summary = "로그인", description = "사용자 로그인 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "활성화 되지 않은 계정입니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/auth/login")
	Response<MemberResponseDTO> login(HttpServletRequest request, HttpServletResponse response, LoginDTO requestDTO);

	@Operation(summary = "로그아웃", description = """
			사용자 로그아웃 API 입니다. \n
			isAllDevice 속성을 통해 모든 기기에서 로그아웃 할 수 있습니다.
			""")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@DeleteMapping("/logout")
	Response<Void> logout(HttpServletRequest request, @RequestBody LogoutDTO requestDTO);

	@Operation(summary = "토큰 재발급", description = "Access Token 재발급 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "토큰 재발급 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = """
					1.토큰이 만료되었습니다. \n
					2.잘못된 토큰입니다.
					""", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/auth/token")
	Response<MemberResponseDTO> reissueAccessToken(HttpServletRequest request, HttpServletResponse response, @Valid @RequestBody ReissueAccessTokenDTO requestDTO);

	@Operation(summary = "비밀번호 변경", description = "사용자 비밀번호 변경 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "가입 시 등록한 이메일로 임시 비밀번호가 발송되었습니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "401", description = "비밀번호가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PatchMapping("/modify-password")
	Response<Void> modifyPassword(@Valid @RequestBody PwdModifyDTO request);
}

