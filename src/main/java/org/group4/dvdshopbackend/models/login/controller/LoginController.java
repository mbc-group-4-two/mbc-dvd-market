package org.group4.dvdshopbackend.models.login.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.core.api.ApiResult;
import org.group4.dvdshopbackend.models.login.dto.LoginReq;
import org.group4.dvdshopbackend.models.login.dto.LoginRes;
import org.group4.dvdshopbackend.models.login.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // 로그인 (세션/쿠키 사용, CSRF 헤더 필요)
    @PostMapping("/logins")
    public ApiResult<LoginRes> login(
            @RequestBody LoginReq req, HttpServletRequest request) {
        var response = loginService.login(req, request);
        return new ApiResult<>(response);
    }

    // 로그아웃 (CSRF 헤더 필요)
    @PostMapping("/logouts")
    public ApiResult<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.logout(request, response);
        return new ApiResult<>(null);
    }


}
