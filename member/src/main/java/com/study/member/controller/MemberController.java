package com.study.member.controller;

import com.study.member.model.request.AddressRequestDTO;
import com.study.member.model.request.WishListRequestDTO;
import com.study.member.model.response.member.MemberResponseDTO;
import com.study.member.model.response.Response;
import com.study.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

	@PostMapping("/{id}")
	public Response<Void> createWishList(@PathVariable Long id, @Valid @RequestBody WishListRequestDTO request) {

		memberService.createWishList(id, request);
		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}

	@PatchMapping("/{id}")
	public Response<Long> modifyWishList(@PathVariable Long id, @RequestParam Integer quantity) {

		return Response.<Long>builder()
				.data(memberService.modifyWishList(id, quantity))
				.build();
	}

	@DeleteMapping("/{id}")
	public Response<Void> deleteWishList(@PathVariable Long id) {

		memberService.deleteWishList(id);
		return Response.<Void>builder()
				.build();
	}

	@PostMapping
	public Response<Void> createAddress(@Valid @RequestBody AddressRequestDTO request) {

		memberService.createAddress(request);
		return Response.<Void>builder()
				.code(HttpStatus.CREATED.value())
				.message(HttpStatus.CREATED.getReasonPhrase())
				.build();
	}
}
