package org.group4.dvdshopbackend.security.auth.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.RefreshTokenHasher;
import org.group4.dvdshopbackend.security.auth.dto.ClientInfo;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginRes;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenReq;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenRes;
import org.group4.dvdshopbackend.security.auth.repository.UserRefreshTokensRepository;
import org.group4.dvdshopbackend.security.jwt.JwtProvider;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
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

		// 일반 비밀번호는 password encoder
		if (!passwordEncoder.matches(req.getUserPassword(), member.getPassword()))
			throw new BadCredentialsException("invalid credentials");

		var tokenVersion = member.getTokenVersion();

		// userId, role, ver 토큰 발급
		var accessToken = jwt.issueAccessToken(member.getId(), member.getRole(), tokenVersion);
		var refreshToken = jwt.issueRefreshToken(member.getId(), tokenVersion);

		//
		var userRefreshToken = jwt.toRefreshTokenEntity(member, refreshToken, clientInfo, refreshTokenHasher);

		// refresh token 저장
		userRefreshTokensRepository.save(userRefreshToken);

		return PerformLoginRes.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	@Override
	@Transactional
	public RefreshTokenRes refreshToken(RefreshTokenReq req, ClientInfo clientInfo) {
		String refreshToken = req.getRefreshToken();

		var token = req.getRefreshToken();

		var claims = jwt.parseRefreshToken(token).getBody();

		// 1. aud == refresh 검증
		if (!claims.getAudience().equals("refresh"))
			throw new BadCredentialsException("invalid credentials");

		// 2. token 분해
		var userId = Long.parseLong(claims.getSubject());
		var ver = claims.get("version", Integer.class);
		var jti = claims.getId(); // UUID

		// 3. token 버전 검증
		var member = memberJpaRepository.findById(userId)
				.orElseThrow(() -> new BadCredentialsException("invalid credentials"));
		if (member.getSnapTokenVersion() != ver) // member 현재 토큰 버전 조회
			throw new BadCredentialsException("invalid credentials");

		// 4. 무결성 검증? + DB Lock
		var oldRefreshToken = userRefreshTokensRepository.findByJtiForUpdate(jti)
				.orElseThrow(() -> new BadCredentialsException("invalid credentials"));

		// --- 멤버 검증
		if ( !oldRefreshToken.getMember().getId().equals(userId))
			throw new BadCredentialsException("unauthorized");

		// --- 교체된 jti 검증
		if (oldRefreshToken.getReplacedDate() != null)
			throw new BadCredentialsException("revoked");

		// --- 토큰 Hash 검증
		if ( !refreshTokenHasher.matches(refreshToken, oldRefreshToken.getTokenHash()))
			throw new BadCredentialsException("mismatch");

		// --- 만료기간 검증
		var now = LocalDateTime.now();
		if ( !oldRefreshToken.getExpiresDate().isAfter(now))
			throw new BadCredentialsException("expired");
		
		// 5. 새 토큰 발급
		var newTokenVersion = member.getTokenVersion(); // 토큰 버전 ++ 됨
		var newAccessToken  = jwt.issueAccessToken(member.getId(), member.getRole(), newTokenVersion);
		var newRefreshToken = jwt.issueRefreshToken(member.getId(), newTokenVersion);

		// 6. 새 토큰을 Redis로 먼저 보냄
		// 토큰가드가 redisKey를 먼저 읽게 되어있는데
		// 계속 옛날 토큰을 읽는 바람에, 버전 미스매치가 계속돼서
		// Refresh Token이 발급됐다가도 바로 401 뜨고 > RT까지 삭제해버리더라구요
		// 그런 뒤에는 다시 로그인해도 AT/RT이 발급됐다가도 로그인만 넘기면 바로 실패로 둘 다 사라짐
		String key = "user:" + userId + ":ver";
		redisTemplate.opsForValue().set(key, Integer.toString(newTokenVersion), Duration.ofMinutes(10));

		// 7. 기존 RT rotate + 저장
		var newClaims = jwt.parseRefreshToken(newRefreshToken).getBody();
		oldRefreshToken.rotate(newClaims.getId());
		userRefreshTokensRepository.save(oldRefreshToken);

		// 8. 신규 RT 저장
		var userRefreshToken = jwt.toRefreshTokenEntity(member, newRefreshToken, clientInfo, refreshTokenHasher);
		userRefreshTokensRepository.save(userRefreshToken);

		return RefreshTokenRes.builder()
				.accessToken(newAccessToken)
				.refreshToken(newRefreshToken)
				.build();
	}
}

/*
 *   // 5. 새 토큰 발급
 *     var newTokenVersion = member.getTokenVersion(); // 토큰 버전 ++ 됨
 *     var newAccessToken = jwt.issueAccessToken(member.getId(), member.getRole(), newTokenVersion);
 *     var newRefreshToken = jwt.issueRefreshToken(member.getId(), newTokenVersion);
 *
 *     var userRefreshToken = jwt.toRefreshTokenEntity(member, newRefreshToken, clientInfo, refreshTokenHasher);
 *
 *     // 신규 refresh token 저장
 *     userRefreshTokensRepository.save(userRefreshToken);
 *
 *     // 6. 기존 refresh token rotate
 *     var newClaims = jwt.parseRefreshToken(newRefreshToken);
 *     oldRefreshToken.rotate(newClaims.getBody().getId());
 *
 *     return RefreshTokenRes.builder()
 *             .accessToken(newAccessToken)
 *             .refreshToken(newRefreshToken)
 *             .build();
 */
