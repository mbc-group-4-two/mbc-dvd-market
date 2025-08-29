package org.group4.dvdshopbackend.config;

import jakarta.servlet.http.HttpServletResponse;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.group4.dvdshopbackend.security.RefreshTokenHasher;
import org.group4.dvdshopbackend.security.TokenGuard;
import org.group4.dvdshopbackend.security.jwt.JwtAuthFilter;
import org.group4.dvdshopbackend.security.jwt.JwtProvider;
import org.group4.dvdshopbackend.security.jwt.config.JwtProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

//    /**
//     * [인증, 인가]
//     * CSRF 인증 방식 = 브라우저 쿠키/세션 기반
//     * @param http
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        boolean isDisable = true;
//
//        if (isDisable) {
//            http.csrf(AbstractHttpConfigurer::disable)
//
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers(HttpMethod.POST, "/api/member/members").permitAll()
//                            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
//                            .requestMatchers("/api/item/**", "/images/**").permitAll()
//                            .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
//                            .anyRequest().permitAll()
//                    );
//            return http.build();
//        } else {
//            // 1) CSRF ON: 쿠키로 토큰 발급(프론트/포스트맨은 헤더 X-XSRF-TOKEN로 전송)
//            http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//
//                    // 2) 경로 권한: 로그인/로그아웃, 공개 리소스만 열고 나머지는 보호
//                    .authorizeHttpRequests(auth -> auth
//                            .requestMatchers("/api/login/**", "/api/member/members", "/api/item/**", "/images/**").permitAll()
//                            .anyRequest().authenticated()
//                    )
//
//                    // 3) 폼/베이직 비활성(우리는 JSON 로그인 사용)
//                    .formLogin(AbstractHttpConfigurer::disable)
//                    .httpBasic(AbstractHttpConfigurer::disable);
//            return http.build();
//        }
//    }

    /**
     * [인증, 인가]
     * JWT + Bearer Token ( JWT는 세션을 쓰지않고 클라이언트에서 토큰을 들고 api 호출시마다 토큰을 보낸다. 모바일앱에서 사용하는 api 혹은 MSA 개발시 사용된다 )
     *
     * @param http
     * @param jwt
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwt) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> {
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/item/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/member/members").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(e -> {
                    e.authenticationEntryPoint((req, res, ex) -> {
                        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    });
                })
                .addFilterBefore(jwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(HttpMethod.POST, "/api/member/members").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
//                        .requestMatchers("/api/item/**", "/images/**").permitAll()
//                        .anyRequest().permitAll()
//                );
//        return http.build();
//    }

    // 테스트용
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                // 1) CSRF ON: 쿠키로 토큰 발급(프론트/포스트맨은 헤더 X-XSRF-TOKEN로 전송)
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//
//                // 2) 경로 권한: 로그인/로그아웃, 공개 리소스만 열고 나머지는 보호
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/login/**", "/api/member/members", "/api/item/**", "/images/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//
//                // 3) 폼/베이직 비활성(우리는 JSON 로그인 사용)
//                .formLogin(f -> f.disable())
//                .httpBasic(b -> b.disable());
//
//        return http.build();
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 테스트용
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager(); // → 내부적으로 UserDetailsService + PasswordEncoder 사용
    }

    @Bean
    CommandLineRunner migrate(MemberJpaRepository repository, PasswordEncoder encoder) {
        return args -> repository.findAll().forEach(m -> {
            String pw = m.getPassword();
            if (pw != null && !pw.startsWith("$2")) {
                m.setPassword(encoder.encode(pw));
            }
        });
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4000"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        cfg.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    JwtAuthFilter jwtAuthFilter(JwtProvider jwt, TokenGuard tokenGuard) { // JwtProvider는 @Component
        return new JwtAuthFilter(jwt, tokenGuard);
    }

}


