package org.group4.dvdshopbackend.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginRes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final MemberJpaRepository memberJpaRepository;

	@Override
	public PerformLoginRes performLogin(PerformLoginReq req) {
		var member = memberJpaRepository.findByEmail(req.getUserId());

		if (member.isEmpty()) {
			return null;
		}
		return PerformLoginRes.builder()
				.memberId(member.orElseThrow().getId())
				.build();
	}
}
