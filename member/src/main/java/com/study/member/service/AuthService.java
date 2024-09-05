package com.study.member.service;

import com.study.member.auth.TokenProvider;
import com.study.member.domain.entity.member.Member;
import com.study.member.exception.member.*;
import com.study.member.exception.token.ExpiredTokenException;
import com.study.member.exception.token.InvalidTokenException;
import com.study.member.model.request.auth.*;
import com.study.member.model.response.member.TokenResponseDTO;
import com.study.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
	public void join(JoinRequestDTO request) {

		memberRepository.findByEmail(request.email()).ifPresent(member -> {
			throw new AlreadyExistEmailException();
		});

		Member member = request.toEntity();

		member.updatePassword(passwordEncoder.encode(member.getPassword()));

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
	public TokenResponseDTO verifyMail(HttpServletRequest request, HttpServletResponse response, JoinVerifyRequestDTO requestDTO) {

		String text = redisService.getTempCertificationTextInRedis(requestDTO.email());

		if (Objects.requireNonNull(text).isEmpty()) {
			throw new ExpiredCertificationNumberException();
		}

		if (!text.equals(requestDTO.certificationText())) {
			throw new NotCorrectCertificationNumberException();
		}

		Member member = memberRepository.findByEmail(requestDTO.email())
				.orElseThrow(NotFoundMemberException::new);

		member.unlockAccount();

		return createMemberResponseDTO(request, response, member);
	}

	@Transactional
	public TokenResponseDTO login(HttpServletRequest request, HttpServletResponse response, LoginRequestDTO requestDTO) {

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
	public void logout(HttpServletRequest request, LogoutRequestDTO requestDTO) {

		Member member = memberRepository.findById(requestDTO.id())
				.orElseThrow(NotFoundMemberException::new);

		if (!StringUtils.hasText(requestDTO.accessToken())) {
			throw new InvalidTokenException(requestDTO.accessToken());
		}

		tokenProvider.parseClaims(requestDTO.accessToken(), member);

		if (requestDTO.isAllDevice()) {

			member.tokenVersionUp();

			redisService.storeTokenVersionInRedis(member.getId().toString(), member.getTokenVersion().toString());
			redisService.deleteAllTokenInRedis(requestDTO.id().toString());
		} else {
			redisService.deleteTokenInRedis(member.getId().toString(), generateDeviceId(request));
		}
	}

	@Transactional
	public TokenResponseDTO reissueAccessToken(HttpServletRequest request, HttpServletResponse response, ReissueTokenRequestDTO requestDTO) {

		Member member = memberRepository.findById(requestDTO.id())
				.orElseThrow(NotFoundMemberException::new);

		String refreshToken = redisService.findRefreshTokenInRedis(generateDeviceId(request));

		if (refreshToken == null || redisService.isBlackList(refreshToken)) {
			throw new ExpiredTokenException("");
		}

		return createMemberResponseDTO(request, response, member);
	}

	@Transactional
	public void modifyPassword(@Valid PwdModifyRequestDTO request) {

		memberRepository.findByEmail(request.email())
				.ifPresentOrElse(member -> {
							if (passwordEncoder.matches(request.oldPwd(), member.getPassword())) {
								member.updatePassword(passwordEncoder.encode(request.newPwd()));
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

	private TokenResponseDTO createMemberResponseDTO(HttpServletRequest request,
	                                                 HttpServletResponse response,
	                                                 Member member) {

		String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRE_TIME_MILLIS);

		response.setHeader(HttpHeaders.AUTHORIZATION, accessToken);
		TokenResponseDTO tokenResponseDTO = tokenProvider.generateTokenResponse(member);

		redisService.storeTokenVersionInRedis(member.getId().toString(), member.getTokenVersion().toString());
		redisService.storeTokenInRedis(
				member.getId().toString(),
				generateDeviceId(request),
				tokenProvider.generateToken(member, REFRESH_TOKEN_EXPIRE_TIME_MILLIS)
		);

		return tokenResponseDTO;
	}
}
