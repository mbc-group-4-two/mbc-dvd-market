package org.group4.dvdshopbackend.security.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * application properties 에서 변수 설정가능
 * @param secret
 * @param accessExpireMs
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secret, long accessExpireMs) {}