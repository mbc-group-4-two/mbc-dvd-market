package org.group4.dvdshopbackend.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginRes;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private static final Logger log = LogManager.getLogger(AuthServiceImpl.class);
	private final MemberJpaRepository memberJpaRepository;
	private final PasswordEncoder passwordEncoder;

//	@Override
//	public PerformLoginRes performLogin(PerformLoginReq req) {
//		var memberResult = memberJpaRepository.findByEmail(req.getUserId());
//
//		if (memberResult.isEmpty()) {
//			return null;
//		}
//
//		var member = memberResult.orElseThrow();
//
//		return PerformLoginRes.builder()
//				.memberId(member.getId())
//				.memberRole(member.getRole())
//				.build();
//	}

	@Override
	public PerformLoginRes performLogin(PerformLoginReq req) {

		log.info(req.getUserId());

		var member = memberJpaRepository.findByEmail(req.getUserId())
				.orElseThrow(() -> new UsernameNotFoundException("user not found"));

		log.info(member.getPassword());
		log.info(passwordEncoder.encode(req.getUserPassword()));

		if (!passwordEncoder.matches(req.getUserPassword(), member.getPassword())) {
			throw new BadCredentialsException("invalid credentials");
		}

		return PerformLoginRes.builder()
				.memberId(member.getId())
				.memberRole(member.getRole())
				.build();
	}
}
