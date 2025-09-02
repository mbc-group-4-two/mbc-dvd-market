package org.group4.dvdshopbackend.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.RefreshTokenHasher;
import org.group4.dvdshopbackend.security.TokenGuard;
import org.group4.dvdshopbackend.security.auth.dto.ClientInfo;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginRes;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenReq;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenRes;
import org.group4.dvdshopbackend.security.auth.repository.UserRefreshTokensRepository;
import org.group4.dvdshopbackend.security.jwt.JwtProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final StringRedisTemplate redisTemplate;

	private final JwtProvider jwt;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenHasher refreshTokenHasher;

	private final MemberJpaRepository memberJpaRepository;
	private final UserRefreshTokensRepository userRefreshTokensRepository;
	private final TokenGuard tokenGuard;

	/**
	 * 로그인 시 호출
	 * member 검증 후
	 * 인증, 인가 정보 토큰 발급
	 * @param req
	 * @param clientInfo
	 * @return
	 */
	@Override
	@Transactional
	public PerformLoginRes performLogin(PerformLoginReq req, ClientInfo clientInfo) {

		var member = memberJpaRepository.findByEmail(req.getUserId())
				.orElseThrow(()-> new BadCredentialsException("invalid credentials"));

		// 1. 비밀번호 검증
		if (!passwordEncoder.matches(req.getUserPassword(), member.getPassword()))
			throw new BadCredentialsException("invalid credentials");

		// 2. token 버전 ++ (잠금)
		var ver = member.getTokenVersion();
		int updated = memberJpaRepository.bumpVersionIfMatch(member.getId(), ver);
		if (updated != 1)
			throw new CredentialsExpiredException("stale token"); // 동시 갱신 차단

		// 3. 신규 토큰 생성
		var newVer = ver + 1;
		var accessToken = jwt.issueAccessToken(member.getId(), member.getRole(), newVer);
		var refreshToken = jwt.issueRefreshToken(member.getId(), newVer);

		var userRefreshToken = jwt.toRefreshTokenEntity(member, refreshToken, clientInfo, refreshTokenHasher);

		// refresh token 저장
		userRefreshTokensRepository.save(userRefreshToken);

		String redisKey = "user:" + member.getId() + ":ver"; // 캐시 DB 토큰 value KEY
		redisTemplate.opsForValue().set(redisKey,
				Integer.toString(newVer),
				java.time.Duration.ofMinutes(15));

		return PerformLoginRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	@Override
	@Transactional
	public RefreshTokenRes refreshToken(RefreshTokenReq req, ClientInfo clientInfo) {
		String refreshToken = req.getRefreshToken();
		var claims = jwt.parseRefreshToken(refreshToken).getBody();

		// refresh token 검증 ( aud, exp )
		tokenGuard.verifyRefresh(claims);


		// 1. token 분해
		var userId = Long.parseLong(claims.getSubject());
		var ver = claims.get("version", Integer.class);
		var jti = claims.getId(); // UUID


		// 2. 무결성 검증? + DB Lock
		var oldRefreshToken = userRefreshTokensRepository.findByJtiForUpdate(jti)
				.orElseThrow(() -> new BadCredentialsException("invalid credentials"));

		// --- 멤버 검증
		if (!oldRefreshToken.getMember().getId().equals(userId))
			throw new BadCredentialsException("unauthorized");

		// --- 교체된 jti 검증
		if (oldRefreshToken.getReplacedDate() != null)
			throw new BadCredentialsException("revoked");

		// --- 토큰 Hash 검증
		if (!refreshTokenHasher.matches(refreshToken, oldRefreshToken.getTokenHash()))
			throw new BadCredentialsException("mismatch");


		// 3. token 버전 ++ (잠금)
		int updated = memberJpaRepository.bumpVersionIfMatch(userId, ver);
		if (updated != 1)
			throw new CredentialsExpiredException("stale token"); // 동시 갱신 차단


		// 4. 신규 토큰 생성
		// 버전은 변수에서 처리
		int newVer = ver + 1;

		// 권한은 권한만 조회,  rotate 를 위한 member 참조는 reference 로 호출한다. (select 쿼리 호출되지 않음)
		var role = memberJpaRepository.findRoleById(userId);
		if (role == null)
			throw new BadCredentialsException("unauthorized");

		var memberRef = memberJpaRepository.getReferenceById(userId);

		var newAccessToken = jwt.issueAccessToken(userId, role, newVer);
		var newRefreshToken = jwt.issueRefreshToken(userId, newVer);

		var userRefreshToken = jwt.toRefreshTokenEntity(memberRef, newRefreshToken, clientInfo, refreshTokenHasher);

		// 신규 refresh token 저장
		userRefreshTokensRepository.save(userRefreshToken);

		String redisKey = "user:" + userId + ":ver"; // 캐시 DB 토큰 value KEY
		redisTemplate.opsForValue().set(redisKey,
				Integer.toString(newVer),
				java.time.Duration.ofMinutes(15));


		// 5. 기존 refresh token rotate
		var newClaims = jwt.parseRefreshToken(newRefreshToken);
		oldRefreshToken.rotate(newClaims.getBody().getId());

		return RefreshTokenRes.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.build();
	}
}