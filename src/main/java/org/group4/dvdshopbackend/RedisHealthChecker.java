package org.group4.dvdshopbackend;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisHealthChecker {

	private final StringRedisTemplate redisTemplate;

	@PostConstruct
	public void checkRedisConnection() {
		try {
			String pong = redisTemplate.getConnectionFactory()
					.getConnection()
					.ping();
			log.info("Redis PING response: {}", pong);  // 성공시 "PONG"
		} catch (Exception e) {
			log.error("Redis connection failed", e);
		}
	}
}
