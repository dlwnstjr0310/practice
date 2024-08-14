package com.study.member.controller;

import com.study.member.controller.docs.MemberOrderControllerDocs;
import com.study.member.model.response.MemberInfoResponseDTO;
import com.study.member.model.response.Response;
import com.study.member.service.MemberOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberOrderController implements MemberOrderControllerDocs {

	private final MemberOrderService memberOrderService;

	@GetMapping("/{id}")
	public Response<MemberInfoResponseDTO> getMemberInfo(@PathVariable Long id) {

		return Response.<MemberInfoResponseDTO>builder()
				.data(memberOrderService.getMemberInfo(id))
				.build();
	}
}
