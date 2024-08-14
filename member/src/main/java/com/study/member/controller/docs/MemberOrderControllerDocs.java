package com.study.member.controller.docs;

import com.study.member.model.response.MemberInfoResponseDTO;
import com.study.member.model.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Member", description = "Order 모듈과 관련된 사용자 관련 API")
public interface MemberOrderControllerDocs {

	@Operation(summary = "사용자 정보 조회", description = "사용자 정보를 가져오는 API 입니다.")
	@GetMapping("/{id}")
	Response<MemberInfoResponseDTO> getMemberInfo(@PathVariable Long id);
}
