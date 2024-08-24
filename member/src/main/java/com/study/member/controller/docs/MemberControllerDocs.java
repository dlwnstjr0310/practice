package com.study.member.controller.docs;

import com.study.member.model.request.member.AddressRequestDTO;
import com.study.member.model.request.member.WishListRequestDTO;
import com.study.member.model.response.Response;
import com.study.member.model.response.member.MemberResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "사용자 관련 API")
public interface MemberControllerDocs {

	@Operation(summary = "마이페이지 조회", description = "위시리스트와 주문 내역을 조회하는 API 입니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = Response.class)))
	@GetMapping("/my-page/{id}")
	Response<MemberResponseDTO> getMemberPage(@PathVariable Long id);

	@Operation(summary = "위시리스트 생성", description = "위시리스트를 생성하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "생성 완료", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "판매중인 상품이 아닙니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = """
					1.존재하지 않는 사용자입니다. \n
					2.해당 상품을 찾을 수 없습니다.
					""", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PostMapping("/{id}")
	Response<Void> createWishList(@PathVariable Long id, @Valid @RequestBody WishListRequestDTO request);

	@Operation(summary = "위시리스트 수량 수정", description = "위시리스트의 수량을 수정하는 API 입니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "수정 완료", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "400", description = "수량은 1개 이상이어야 합니다.", content = @Content(schema = @Schema(implementation = Response.class))),
			@ApiResponse(responseCode = "404", description = "존재하지 않는 위시리스트입니다.", content = @Content(schema = @Schema(implementation = Response.class)))
	})
	@PatchMapping("/{id}")
	Response<Long> modifyWishList(@PathVariable Long id, @RequestParam Integer quantity);

	@Operation(summary = "위시리스트 삭제", description = "위시리스트를 삭제하는 API 입니다.")
	@ApiResponse(responseCode = "200", description = "삭제 완료", content = @Content(schema = @Schema(implementation = Response.class)))
	@DeleteMapping("/{id}")
	Response<Void> deleteWishList(@PathVariable Long id);

	@Operation(summary = "배송지 추가", description = "회원의 배송지를 추가하는 API 입니다.")
	@ApiResponse(responseCode = "201", description = "추가 완료", content = @Content(schema = @Schema(implementation = Response.class)))
	@PostMapping("/address")
	Response<Void> createAddress(@Valid @RequestBody AddressRequestDTO request);

}
