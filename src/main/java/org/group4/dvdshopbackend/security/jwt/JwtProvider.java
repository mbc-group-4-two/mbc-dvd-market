package org.group4.dvdshopbackend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.group4.dvdshopbackend.common.entity.Member;
import org.group4.dvdshopbackend.common.enums.Role;
import org.group4.dvdshopbackend.security.RefreshTokenHasher;
import org.group4.dvdshopbackend.security.auth.dto.ClientInfo;
import org.group4.dvdshopbackend.security.auth.domain.UserRefreshToken;
import org.group4.dvdshopbackend.security.auth.record.LoginUser;
import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Component
@Log4j2
public class JwtProvider {

	private final SecretKey accessKey;
	private final long accessExpireMs;

	private final SecretKey refreshKey;
	private final long refreshExpireMs;

	public JwtProvider(JwtProperties props) {
		this.accessKey = Keys.hmacShaKeyFor(props.accessSecret().getBytes(StandardCharsets.UTF_8));
		this.accessExpireMs = props.accessExpireMs();
		this.refreshKey = Keys.hmacShaKeyFor(props.refreshSecret().getBytes(StandardCharsets.UTF_8));
		this.refreshExpireMs = props.refreshExpireMs();
	}

	/**
	 * 토큰 발급시 Entity
	 * @param member
	 * @param refreshToken
	 * @param clientInfo
	 * @return
	 */
	public UserRefreshToken toRefreshTokenEntity(Member member, String refreshToken, ClientInfo clientInfo, RefreshTokenHasher tokenHasher) {

		var tokenClaims = parseRefreshToken(refreshToken).getBody();
		var expLdt = Instant.ofEpochMilli(tokenClaims.getExpiration().getTime())
				.atZone(ZoneId.of("UTC"))
				.toLocalDateTime();

		var entity = new UserRefreshToken();
		entity.setMember(member);
		entity.setJti(tokenClaims.getId());
		entity.setTokenHash(tokenHasher.hash(refreshToken));
		entity.setCreatedDate(LocalDateTime.now());
		entity.setExpiresDate(expLdt);
		entity.setDeviceId(clientInfo.getDeviceId());
		entity.setUserAgent(clientInfo.getUserAgent());
		entity.setIp(clientInfo.getIp());

		return entity;
	}

	public String issueAccessToken(Long userId, Role role, int ver) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("role", role)
				.claim("version", ver)

				.setId(UUID.randomUUID().toString())
				.setAudience("access")
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + accessExpireMs))

				.signWith(accessKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public String issueRefreshToken(Long userId, int ver) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("version", ver)

				.setId(UUID.randomUUID().toString())
				.setAudience("refresh")
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + refreshExpireMs))

				.signWith(refreshKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public Jws<Claims> parseAccessToken(String token) {
		return Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(token);
	}

	public Jws<Claims> parseRefreshToken(String token) {
		return Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(token);
	}

	@Deprecated
	public Long getUserId(String token) {
		return Long.valueOf(parseAccessToken(token).getBody().getSubject());
	}

	@Deprecated
	public String getRole(String token) {
		return parseAccessToken(token).getBody().get("role", String.class);
	}

	public LoginUser getLoginUser(String token) {
		var claims = parseAccessToken(token).getBody();

		var id = Long.valueOf(claims.getSubject());
		var role = claims.get("role", String.class);
		return new LoginUser(id, role);
	}
}
