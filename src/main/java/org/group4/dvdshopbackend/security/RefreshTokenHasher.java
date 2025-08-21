package org.group4.dvdshopbackend.security;

import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class RefreshTokenHasher {
	private final Mac mac;

	public RefreshTokenHasher(JwtProperties props) throws NoSuchAlgorithmException, InvalidKeyException {
		mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(props.refreshSecret().getBytes(UTF_8), "HmacSHA256"));
	}

	public String hash(String token) {
		return HexFormat.of().formatHex(mac.doFinal(token.getBytes(UTF_8)));
	}

	public boolean matches(String token, String storedHex) {
		var a = mac.doFinal(token.getBytes(UTF_8));
		var b = HexFormat.of().parseHex(storedHex);
		return MessageDigest.isEqual(a, b);
	}
}
