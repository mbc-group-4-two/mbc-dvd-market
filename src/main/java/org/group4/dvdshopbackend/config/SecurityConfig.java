package org.group4.dvdshopbackend.config;

import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/item/**", "/images/**").permitAll()
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

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
}
