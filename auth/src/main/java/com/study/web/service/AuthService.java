package com.study.web.service;

import com.study.web.auth.TokenProvider;
import com.study.web.domain.entity.Member;
import com.study.web.domain.entity.MemberRole;
import com.study.web.exception.login.*;
import com.study.web.model.request.*;
import com.study.web.model.response.MemberResponseDTO;
import com.study.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

	private static final long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = 60L * 60L * 1000L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = 14L * 24L * 60L * 60L * 1000L;

	private final MemberRepository memberRepository;

	private final TokenProvider tokenProvider;
	private final RedisService redisService;
	private final PasswordEncoder passwordEncoder;
	private final MailCertificationService mailService;

	@Transactional
	public void join(JoinDTO request) {

		memberRepository.findByEmail(request.email()).ifPresent(member -> {
			throw new AlreadyExistEmailException();
		});

		Member member = request.toEntity();

		//todo: 개인정보도 암호화 해야하는데 너무귀찮음
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
	public MemberResponseDTO verifyMail(VerifyDTO request) {

		String text = redisService.getTempCertificationTextInRedis(request.email());

		if (Objects.requireNonNull(text).isEmpty()) {
			throw new ExpiredCertificationNumberException();
		}

		if (!text.equals(request.certificationText())) {
			throw new NotCorrectCertificationNumberException();
		}

		Member member = memberRepository.findByEmail(request.email())
				.orElseThrow(NotFoundMemberException::new);

		member.setIsLocked(false);
		member.setMemberRole(MemberRole.ROLE_BUYER);
		String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRE_TIME_MILLIS);

		redisService.storeRefreshTokenInRedis(
				member.getId().toString(),
				accessToken,
				tokenProvider.generateToken(member, REFRESH_TOKEN_EXPIRE_TIME_MILLIS)
		);

		return new MemberResponseDTO(
				accessToken,
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}

	@Transactional
	public MemberResponseDTO login(LoginDTO request) {

		Member member = memberRepository.findByEmail(request.email())
				.orElseThrow(NotFoundMemberException::new);

		if (member.getIsLocked()) {
			throw new AccountNotEnabledException();
		}

		if (!passwordEncoder.matches(request.password(), member.getPassword())) {
			throw new InvalidPasswordException();
		}

		String accessToken = tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRE_TIME_MILLIS);

		redisService.storeRefreshTokenInRedis(
				member.getId().toString(),
				accessToken,
				tokenProvider.generateToken(member, REFRESH_TOKEN_EXPIRE_TIME_MILLIS)
		);

		return new MemberResponseDTO(
				accessToken,
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}

	@Transactional
	public void logout(LogoutDTO request) {

		if (request.isAllDevice()) {
			redisService.deleteTargetInRedis(request.id().toString());
		} else {
			redisService.deleteTargetInRedis(request.id().toString(), request.accessToken());
		}
	}

	@Transactional
	public MemberResponseDTO reissueAccessToken(ReissueAccessTokenDTO request) {

		// 여기서도 에러나면 걍 로그인 다시해야함
		redisService.findRefreshTokenInRedis(request.id().toString(), request.accessToken());

		Member member = memberRepository.findById(request.id())
				.orElseThrow(NotFoundMemberException::new);

		return new MemberResponseDTO(
				tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRE_TIME_MILLIS),
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}

	@Transactional
	public void modifyPassword(LoginDTO request) {

		memberRepository.findByEmail(request.email())
				.ifPresentOrElse(member -> {
							if (passwordEncoder.matches(request.password(), member.getPassword())) {
								mailService.sendMail(request.email(), false);
							} else {
								throw new InvalidPasswordException();
							}
						}
						, () -> {
							throw new NotFoundMemberException();
						}
				);
	}
}
