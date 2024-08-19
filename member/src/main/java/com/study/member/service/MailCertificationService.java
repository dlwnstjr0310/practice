package com.study.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailCertificationService {

	private final JavaMailSender mailSender;

	public String sendMail(String to, boolean isJoin) {

		String tempCertificationText = isJoin ? issueCertificationNumber() : issueTempPassword();

		SimpleMailMessage message = new SimpleMailMessage();

		message.setTo(to);
		message.setText(tempCertificationText);

		mailSender.send(message);
		return tempCertificationText;
	}

	public String issueCertificationNumber() {
		StringBuilder certificationNumber = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			certificationNumber.append((int) (Math.random() * 10));
		}

		return certificationNumber.toString();
	}

	private String issueTempPassword() {

		char[] charSet = new char[]{
				'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
		};

		StringBuilder tempPassword = new StringBuilder();

		Random random = new Random();

		for (int i = 0; i < 8; i++) {
			char randomWord = charSet[random.nextInt(charSet.length)];
			tempPassword.append(randomWord);
		}

		return tempPassword.toString();
	}

}
