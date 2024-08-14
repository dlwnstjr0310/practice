package com.study.member.controller;

import com.study.member.model.response.MemberResponseDTO;
import com.study.member.model.response.Response;
import com.study.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/my-page/{id}")
	public Response<MemberResponseDTO> getMemberPage(@PathVariable Long id) {

		return Response.<MemberResponseDTO>builder()
				.data(memberService.getMemberPage(id))
				.build();
	}
}
