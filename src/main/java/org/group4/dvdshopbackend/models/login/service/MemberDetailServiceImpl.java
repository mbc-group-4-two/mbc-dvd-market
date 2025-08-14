package org.group4.dvdshopbackend.models.login.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Member;
import org.group4.dvdshopbackend.models.member.repository.MemberJpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberDetailServiceImpl implements UserDetailsService {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 이메일로 사용자 조회
        Member member = memberJpaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));

        // 소프트 딜리트된 계정이면 인증 거부
        if ("Y".equalsIgnoreCase(member.getDeletedYn())) {
            throw new UsernameNotFoundException("User is deleted: " + email);
        }

        // 스프링 시큐리티가 사용할 UserDetails 구현체로 포장
        return new CustomUserDetails(member);
    }
}
