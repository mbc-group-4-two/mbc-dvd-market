package org.group4.dvdshopbackend.models.login.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.models.login.dto.LoginReq;
import org.group4.dvdshopbackend.models.login.dto.LoginRes;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    
    private final AuthenticationManager authenticationManager;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public LoginRes login(LoginReq request, HttpServletRequest httpRequest) {
        String email = request.getEmail().trim().toLowerCase();

        // 1) 인증 시도(비밀번호 검증 포함). 실패 시 예외 발생
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPassword())
        );

        // 2) 세션 저장
        SecurityContextHolder.getContext().setAuthentication(auth);
        httpRequest.getSession(true);

        // 3) 응답 정보 구성
        var member = memberJpaRepository.findByEmailAndDeletedYn(email, "N").orElseThrow();
        if ("Y".equalsIgnoreCase(member.getDeletedYn())) throw new IllegalStateException("비활성화된 회원입니다.");
        return LoginRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole() != null ? member.getRole().name() : null)
                .build();
    }

    @Override
    public void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        var logout = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(httpRequest, httpResponse, logout);


    }
}
