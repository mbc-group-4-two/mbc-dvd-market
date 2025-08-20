package org.group4.dvdshopbackend.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginRes;
import org.group4.dvdshopbackend.security.auth.repository.UserRefreshTokensRepository;
import org.group4.dvdshopbackend.security.jwt.JwtProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final JwtProvider jwt;
	private final PasswordEncoder passwordEncoder;

	private final MemberJpaRepository memberJpaRepository;
	private final UserRefreshTokensRepository userRefreshTokensRepository;

	@Override
	public PerformLoginRes performLogin(PerformLoginReq req) {

		var member = memberJpaRepository.findByEmail(req.getUserId())
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));

		if (!passwordEncoder.matches(req.getUserPassword(), member.getPassword()))
			throw new BadCredentialsException("invalid credentials");

		var generatedAccessToken = jwt.generateAccessToken(member.getId(), member.getRole());
		var generatedRefreshToken = jwt.generateRefreshToken(member.getId());



//		member.

		return PerformLoginRes.builder()
				.accessToken(generatedAccessToken)
				.refreshToken(generatedRefreshToken)
				.build();
	}
}
