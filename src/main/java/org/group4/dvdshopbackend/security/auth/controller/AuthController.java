package org.group4.dvdshopbackend.security.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.group4.dvdshopbackend.core.api.ApiResponse;
import org.group4.dvdshopbackend.security.auth.dto.ClientInfo;
import org.group4.dvdshopbackend.security.auth.dto.performLogin.PerformLoginReq;
import org.group4.dvdshopbackend.security.auth.dto.refreshToken.RefreshTokenReq;
import org.group4.dvdshopbackend.security.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/api/auth/v1", "/api/auth"})
@RequiredArgsConstructor
@Log4j2
public class AuthController {

	private final AuthService authService;


	@PostMapping("/login")
	public ResponseEntity<?> performLogin(@RequestBody PerformLoginReq request,
	                                      HttpServletRequest httpRequest) {

		var clientInfo = new ClientInfo(httpRequest);
		var res = authService.performLogin(request, clientInfo);

		return ApiResponse.ok(res);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenReq request,
	                                      HttpServletRequest httpRequest) {

		// RTR (Refresh Token Rotation)
		var clientInfo = new ClientInfo(httpRequest);
		var res = authService.refreshToken(request, clientInfo);

		return ApiResponse.ok(res);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		// refresh 토큰 폐기
		return ApiResponse.ok(null);
	}
}

