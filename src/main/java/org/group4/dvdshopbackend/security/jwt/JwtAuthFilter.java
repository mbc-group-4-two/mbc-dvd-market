package org.group4.dvdshopbackend.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwt;

	public JwtAuthFilter(JwtProvider jwt) {
		this.jwt = jwt;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		String h = req.getHeader("Authorization");
		if (h == null || !h.startsWith("Bearer ")) {
			chain.doFilter(req, res);
			return;
		}

		String token = h.substring(7);

		try {
			if (!jwt.validate(token)) {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return;
			}

			Long userId = jwt.getUserId(token);           // sub 또는 claim에서 추출
			String role = jwt.getRole(token);             // "ROLE_USER" 형태로 추출

			List<GrantedAuthority> authorities =
					AuthorityUtils.createAuthorityList("ROLE_" + role);

			var principal = new LoginUser(userId, role);

			UsernamePasswordAuthenticationToken auth =
					new UsernamePasswordAuthenticationToken(principal, null, authorities);
			auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);

			SecurityContextHolder.setContext(context);

			chain.doFilter(req, res);
		} catch (Exception e) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
