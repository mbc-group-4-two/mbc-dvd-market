package org.group4.dvdshopbackend.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.group4.dvdshopbackend.security.TokenGuard;
import org.group4.dvdshopbackend.security.auth.record.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwt;
	private final TokenGuard tokenGuard; // redis 적용 필요

	/**
	 * JWT 토큰 인증
	 * Access 토큰만 검증하며
	 *
	 * /auth/refresh = 인증 대상이 아닌 api ( /auth/** )
	 * Service 에서 검증한다.
	 *
	 * @param req
	 * @param res
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		// CORS 등 preflight 요청은 JWT 인증 skip
		if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
			chain.doFilter(req, res);
			return;
		}

		// 다른 Filter 에서 상위 인증을 통해 SecurityContext 가 이미 주입된 경우 JWT 인증 skip
		if (SecurityContextHolder.getContext().getAuthentication() != null) {
			chain.doFilter(req, res);
			return;
		}

		// 헤더가 Authorization Bearer 가 아닌경우 JWT 인증 skip
		// 그냥 skip 시켜도 이후 authorizeHttpRequests 단계에서 EntryPoint 를 통해 미인증 보호 API 요청에 401 리턴
		String HEADER_STRING = "Authorization";
		String TOKEN_PREFIX = "Bearer ";

		var header = req.getHeader(HEADER_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		// 토큰 인증 로직
		// Access 토큰 검증은  컨트롤러로 넘기지 말것.  redis 캐시 DB 등을 사용해 필터 내에서 검증 끝낼것.
		// 임시 처리로 일단 TokenGuard -> DB 넘김 +++++
		var token = header.substring(TOKEN_PREFIX.length());

		try {
			var claims = jwt.parseAccessToken(token).getBody();
			tokenGuard.verify(claims);

			var principal = jwt.getLoginUser(token);
			var authorities = AuthorityUtils.createAuthorityList("ROLE_" + principal.role());

			// Token 인증 기반은 credentials 필요 없음 -> null 처리
			var auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

			// 해당 endpoint 요청에 대한 Security Context 유지 설정
			var context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);

			chain.doFilter(req, res);

		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
