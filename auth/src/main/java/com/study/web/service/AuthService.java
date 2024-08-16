package com.study.web.service;

import com.study.web.auth.TokenProvider;
import com.study.web.domain.entity.Member;
import com.study.web.domain.entity.MemberRole;
import com.study.web.exception.login.*;
import com.study.web.exception.token.ExpiredTokenException;
import com.study.web.model.request.*;
import com.study.web.model.response.MemberResponseDTO;
import com.study.web.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

	private static final String USER_AGENT = "User-Agent";
	private static final long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = 14L * 24L * 60L * 60L * 1000L;
	private static final long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = 60L * 60L * 1000L;

	private final RedisService redisService;
	private final TokenProvider tokenProvider;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailCertificationService mailService;

	@Transactional
	public void join(JoinDTO request) {

		memberRepository.findByEmail(request.email()).ifPresent(member -> {
			throw new AlreadyExistEmailException();
		});

		Member member = request.toEntity();

		//todo: 개인정보도 암호화 해야하는데 너무귀찮음
		//todo: 여기서 주소 등록까지 해야할듯?
		member.setPassword(passwordEncoder.encode(member.getPassword()));

		memberRepository.save(member);
	}

	@Transactional
	public void sendMail(String email) {

		redisService.storeTempCertificationTextInRedis(
				email,
				mailService.sendMail(email, true)
		);
	}

	@Transactional
	public MemberResponseDTO verifyMail(HttpServletRequest request, HttpServletResponse response, VerifyDTO requestDTO) {

		String text = redisService.getTempCertificationTextInRedis(requestDTO.email());

		if (Objects.requireNonNull(text).isEmpty()) {
			throw new ExpiredCertificationNumberException();
		}

		if (!text.equals(requestDTO.certificationText())) {
			throw new NotCorrectCertificationNumberException();
		}

		Member member = memberRepository.findByEmail(requestDTO.email())
				.orElseThrow(NotFoundMemberException::new);

		member.setIsLocked(false);
		member.setMemberRole(MemberRole.ROLE_BUYER);

		return createMemberResponseDTO(request, response, member);
	}

	@Transactional
	public MemberResponseDTO login(HttpServletRequest request, HttpServletResponse response, LoginDTO requestDTO) {

		Member member = memberRepository.findByEmail(requestDTO.email())
				.orElseThrow(NotFoundMemberException::new);

		if (member.getIsLocked()) {
			throw new AccountNotEnabledException();
		}

		if (!passwordEncoder.matches(requestDTO.password(), member.getPassword())) {
			throw new InvalidPasswordException();
		}

		return createMemberResponseDTO(request, response, member);
	}

	@Transactional
	public void logout(HttpServletRequest request, LogoutDTO requestDTO) {

		Member member = memberRepository.findById(requestDTO.id())
				.orElseThrow(NotFoundMemberException::new);

		tokenProvider.parseClaims(tokenProvider.getJwt(requestDTO.accessToken()), member);

		if (requestDTO.isAllDevice()) {

			member.setTokenVersion(member.getTokenVersion() + 1);

			// tokenVersion 업데이트 : 블랙리스트 등록 필요없음
			redisService.storeTokenVersionInRedis(member.getId().toString(), member.getTokenVersion());
			// 기존 device 및 refreshToken 모두 제거
			redisService.deleteAllTokenInRedis(requestDTO.id().toString());
		} else {
			// 단일 디바이스 로그아웃 및 토큰 블랙리스트 등록
			redisService.deleteTokenInRedis(member.getId().toString(), generateDeviceId(request));
		}
	}

	@Transactional
	public MemberResponseDTO reissueAccessToken(HttpServletRequest request, HttpServletResponse response, ReissueAccessTokenDTO requestDTO) {

		Member member = memberRepository.findById(requestDTO.id())
				.orElseThrow(NotFoundMemberException::new);

		String refreshToken = redisService.findRefreshTokenInRedis(generateDeviceId(request));

		if (refreshToken == null || redisService.isBlackList(refreshToken)) {
			throw new ExpiredTokenException("");
		}

		return createMemberResponseDTO(request, response, member);
	}

	@Transactional
	public void modifyPassword(@Valid PwdModifyDTO request) {

		memberRepository.findByEmail(request.email())
				.ifPresentOrElse(member -> {
							if (passwordEncoder.matches(request.oldPwd(), member.getPassword())) {
								member.setPassword(passwordEncoder.encode(request.newPwd()));
								memberRepository.save(member);
							} else {
								throw new InvalidPasswordException();
							}
						}
						, () -> {
							throw new NotFoundMemberException();
						}
				);

	}

	private String generateDeviceId(HttpServletRequest request) {
		return UUID.nameUUIDFromBytes(request.getHeader(USER_AGENT).getBytes()).toString();
	}

	private MemberResponseDTO createMemberResponseDTO(HttpServletRequest request,
	                                                  HttpServletResponse response,
	                                                  Member member) {

		response.setHeader(HttpHeaders.AUTHORIZATION, tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRE_TIME_MILLIS));
		MemberResponseDTO memberResponseDTO = tokenProvider.generateTokenResponse(member);

		redisService.storeTokenVersionInRedis(member.getId().toString(), member.getTokenVersion());
		redisService.storeTokenInRedis(
				member.getId().toString(),
				generateDeviceId(request),
				tokenProvider.generateToken(member, REFRESH_TOKEN_EXPIRE_TIME_MILLIS)
		);

		return memberResponseDTO;
	}
}
