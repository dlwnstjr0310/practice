package com.study.member.controller.docs;

import com.study.member.model.response.member.MemberInfoResponseDTO;
import com.study.member.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Member", description = "Order 모듈과 관련된 사용자 관련 API")
public interface MemberOrderControllerDocs {

	@Operation(summary = "사용자 정보 조회", description = "사용자 정보를 가져오는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 회원입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@GetMapping("/{id}")
	Response<MemberInfoResponseDTO> getMemberInfo(@PathVariable Long id);
}
