package com.study.web.service;

import com.study.web.auth.TokenProvider;
import com.study.web.domain.entity.Member;
import com.study.web.exception.login.AlreadyExistEmailException;
import com.study.web.exception.login.ExpiredCertificationNumberException;
import com.study.web.exception.login.NotCorrectCertificationNumberException;
import com.study.web.exception.login.NotFoundMemberException;
import com.study.web.model.request.MemberRequest;
import com.study.web.model.response.LoginResponse;
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
	public LoginResponse.Info verifyMail(MemberRequest.Verify request) {

		String text = redisService.getTempCertificationTextInRedis(request);

		if (Objects.requireNonNull(text).isEmpty()) {
			throw new ExpiredCertificationNumberException();
		}

		if (!text.equals(request.certificationText())) {
			throw new NotCorrectCertificationNumberException();
		}

		// AT, RT 발급 및 유저정보 리턴
		Member member = memberRepository.findByEmail(request.email())
				.orElseThrow(NotFoundMemberException::new);

		member.setIsLocked(false);

		redisService.storeRefreshTokenInRedis(
				member.getId().toString(),
				tokenProvider.generateToken(member, REFRESH_TOKEN_EXPIRE_TIME_MILLIS)
		);

		return new LoginResponse.Info(
				tokenProvider.generateToken(member, ACCESS_TOKEN_EXPIRE_TIME_MILLIS),
				member.getId(),
				member.getEmail(),
				member.getName()
		);
	}
}
