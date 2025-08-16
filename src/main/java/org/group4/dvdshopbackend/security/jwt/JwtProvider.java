package org.group4.dvdshopbackend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
	private final SecretKey key;
	private final long expireMs;

	public JwtProvider(JwtProperties props) {
//		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.secret()));
		this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
		this.expireMs = props.accessExpireMs();
	}

	public String generateAccessToken(Long userId) {
		long now = System.currentTimeMillis();
		return Jwts.builder()
				.setSubject(String.valueOf(userId))
				.setIssuedAt(new Date(now))
				.setExpiration(new Date(now + expireMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public Jws<Claims> parse(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
	}

	public boolean validate(String token) {
		try {
			parse(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public Long getUserId(String token) {
		return Long.valueOf(parse(token).getBody().getSubject());
	}
}
