package com.study.web.service;

import com.study.web.auth.TokenProvider;
import com.study.web.domain.entity.Member;
import com.study.web.domain.entity.MemberRole;
import com.study.web.exception.login.*;
import com.study.web.model.request.MemberRequest;
import com.study.web.model.response.MemberResponse;
import com.study.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

	private static final long ACCESS_TOKEN_EXPIRE_TIME_MILLIS = 60L * 60L * 1000L;
	private static final long REFRESH_TOKEN_EXPIRE_TIME_MILLIS = 14L * 24L * 60L * 60L * 1000L;
	private final TokenProvider tokenProvider;
	private final RedisService redisService;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MailCertificationService mailService;

	@Transactional
	public void join(MemberRequest.Join request) {

		memberRepository.findByEmail(request.email()).ifPresent(member -> {
			throw new AlreadyExistEmailException();
		});

		Member member = request.toEntity();

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
	public MemberResponse.Info verifyMail(MemberRequest.Verify request) {

		String text = redisService.getTempCertificationTextInRedis(request.email());

		log.info("구조 확인좀 : {} ", text);
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

		return new MemberResponse.Info(
				accessToken,
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}

	@Transactional
	public MemberResponse.Info login(MemberRequest.Login request) {

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

		return new MemberResponse.Info(
				accessToken,
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}

	@Transactional
	public void logout(MemberRequest.Logout request) {

		if (request.isAllDevice()) {
			redisService.deleteTargetInRedis(request.id().toString());
		} else {
			redisService.deleteTargetInRedis(request.id().toString(), request.accessToken());
		}
	}
}
