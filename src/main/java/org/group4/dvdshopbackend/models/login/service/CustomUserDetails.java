package org.group4.dvdshopbackend.models.login.service;

import lombok.RequiredArgsConstructor;
import org.group4.dvdshopbackend.common.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

// 테스트용

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;

    public Long getMemberId() { return member.getId(); }
    public String getEmail()  { return member.getEmail(); }
    public String getName()   { return member.getName(); }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = member.getRole() != null ? member.getRole().name() : "USER";
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }
    @Override public String getPassword()              { return member.getPassword(); }
    @Override public String getUsername()              { return member.getEmail(); }
    @Override public boolean isAccountNonExpired()     { return true; }
    @Override public boolean isAccountNonLocked()      { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled()               { return !"Y".equalsIgnoreCase(member.getDeletedYn()); }

}
