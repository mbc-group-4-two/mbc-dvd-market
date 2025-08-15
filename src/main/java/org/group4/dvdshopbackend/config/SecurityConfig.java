package org.group4.dvdshopbackend.config;

import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        boolean isDisable = true;

        if (isDisable) {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers(HttpMethod.POST, "/api/member/members").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                            .requestMatchers("/api/item/**", "/images/**").permitAll()
                            .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                            .anyRequest().permitAll()
                    );
            return http.build();
        } else {
            // 1) CSRF ON: 쿠키로 토큰 발급(프론트/포스트맨은 헤더 X-XSRF-TOKEN로 전송)
            http.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

                    // 2) 경로 권한: 로그인/로그아웃, 공개 리소스만 열고 나머지는 보호
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/login/**", "/api/member/members", "/api/item/**", "/images/**").permitAll()
                            .anyRequest().authenticated()
                    )

                    // 3) 폼/베이직 비활성(우리는 JSON 로그인 사용)
                    .formLogin(AbstractHttpConfigurer::disable)
                    .httpBasic(AbstractHttpConfigurer::disable);
            return http.build();
        }
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
        // 프론트에서 접속하는 모든 오리진을 등록
        cfg.setAllowedOrigins(List.of(
                "http://192.168.0.173:3000",
                "http://localhost:3000"
        ));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        // (필요 시) 노출할 헤더 추가
        // cfg.setExposedHeaders(List.of("Content-Disposition"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;

    }
}


