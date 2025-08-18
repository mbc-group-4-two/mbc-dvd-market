package org.group4.dvdshopbackend.security.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.group4.dvdshopbackend.core.api.ApiError;
import org.group4.dvdshopbackend.core.api.ApiResponse;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.PerformLoginRes;
import org.group4.dvdshopbackend.security.auth.service.AuthService;
import org.group4.dvdshopbackend.security.jwt.JwtProvider;
import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/auth/v1")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

	private final JwtProvider jwt;
	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> performLogin(@RequestBody PerformLoginReq req) {

		var res = authService.performLogin(req);

		if (res == null)
			throw new NoSuchElementException();

		var userId = res.getMemberId();
		var newToken = jwt.generateAccessToken(userId);

		return ApiResponse.ok(PerformLoginRes.builder()
				.accessToken(newToken)
				.build());
	}
}
