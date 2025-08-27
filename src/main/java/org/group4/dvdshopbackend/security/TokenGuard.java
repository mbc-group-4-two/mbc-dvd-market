package org.group4.dvdshopbackend.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TokenGuard {

	private static final Logger log = LogManager.getLogger(TokenGuard.class);
	private final MemberJpaRepository memberJpaRepository;

	private final StringRedisTemplate redisTemplate;

//	private final Cache<Long, Integer> verCache = Caffeine.newBuilder()
//			.expireAfterWrite(Duration.ofMinutes(5)).maximumSize(100_000).build();

//	public void verify(Claims c) {
//		log.info("tokenGuard verify in.");
//		long uid = Long.parseLong(c.getSubject());
//		int tokenVersion = c.get("version", Integer.class);
//		int currentVersion = memberJpaRepository.findTokenVersionById(uid).intValue();
//
//		log.info("tokenVersion:" + tokenVersion);
//		log.info("currentVersion:" + currentVersion);
//
//		// aud 검증
//		if (!"access".equals(c.getAudience())) {
//			throw new BadCredentialsException("not access token");
//		}
//
//		log.info("tokenVersion:" + tokenVersion);
//		log.info("currentVersion:" + currentVersion);
//
//		// 버전 검증
//		if (tokenVersion != currentVersion)
//			throw new CredentialsExpiredException("ver mismatch");
//	}

	public void verify(Claims c) {
		log.info("tokenGuard verify in.");
		long uid = Long.parseLong(c.getSubject());
		Integer tokenVersion = c.get("version", Integer.class);

		// aud 검증
		if (!"access".equals(c.getAudience())) {
			throw new BadCredentialsException("not access token");
		}

		String redisKey = "user:" + uid + ":ver";
		String redisValue = redisTemplate.opsForValue().get(redisKey);
		int currentVersion = (redisValue != null) ?
				Integer.parseInt(redisValue) : memberJpaRepository.findTokenVersionById(uid).intValue();

		if (redisValue == null)
			redisTemplate.opsForValue().set(redisKey,
							Integer.toString(currentVersion),
							java.time.Duration.ofMinutes(10));

		// 버전 검증
		if (tokenVersion == null || tokenVersion != currentVersion)
			throw new CredentialsExpiredException("ver mismatch");
	}
}
