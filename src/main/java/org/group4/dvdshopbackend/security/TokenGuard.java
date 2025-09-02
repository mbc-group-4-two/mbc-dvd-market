package org.group4.dvdshopbackend.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenGuard {

	private final JwtProperties jwtProperties;
	private final StringRedisTemplate redisTemplate;
	private final MemberJpaRepository memberJpaRepository;

	/**
	 * 토큰 버전 검증
	 * @param uid
	 * @param tokenVersion
	 */
	private void checkVersion(long uid, Integer tokenVersion) {
		String redisKey = "user:" + uid + ":ver"; // 캐시 DB 토큰 value KEY
		String redisValue = redisTemplate.opsForValue().get(redisKey);
		int currentVersion = (redisValue != null) ?
				Integer.parseInt(redisValue) : memberJpaRepository.findTokenVersionById(uid).intValue();

		if (redisValue == null)
			redisTemplate.opsForValue().set(redisKey,
					Integer.toString(currentVersion),
					java.time.Duration.ofMinutes(15));

		if (tokenVersion == null || tokenVersion != currentVersion)
			throw new CredentialsExpiredException("ver mismatch");
	}

	/**
	 * Controller 요청이 오면 JWT Filter 에서 에서 토큰을 검증한다.
	 *
	 * 캐시 DB를 통해 사용자의 Token 버전을 조회 해보고,
	 * 기록이 없으면 실제 DB를 통해 사용자 조회 실행 ( 실제 DB에 부하 감소 )
	 * @param c
	 */
	public void verify(Claims c) {
		long uid = Long.parseLong(c.getSubject());
		Integer tokenVersion = c.get("version", Integer.class);

		// aud 검증
		if (!"access".equals(c.getAudience())) {
			throw new BadCredentialsException("not access token");
		}

		// token 내 만료기간 없음
		Date expDate = c.getExpiration();
		if (expDate == null)
			throw new BadCredentialsException("exp missing");

		// access token 만료
		if (Instant.now().isAfter(expDate.toInstant().plusSeconds(jwtProperties.accessExpireMs())))
			throw new CredentialsExpiredException("expired");

		checkVersion(uid, tokenVersion);
	}

	public void verifyRefresh(Claims c) {
		long uid = Long.parseLong(c.getSubject());
		Integer tokenVersion = c.get("version", Integer.class);

		// aud == refresh 검증
		if (!"refresh".equals(c.getAudience())) {
			throw new BadCredentialsException("not refresh token");
		}

		// token 내 만료기간 없음
		Date expDate = c.getExpiration();
		if (expDate == null)
			throw new BadCredentialsException("exp missing");

		// refresh token 만료
		if (Instant.now().isAfter(expDate.toInstant().plusSeconds(jwtProperties.refreshExpireMs())))
			throw new CredentialsExpiredException("expired");

		checkVersion(uid, tokenVersion);
	}
}
