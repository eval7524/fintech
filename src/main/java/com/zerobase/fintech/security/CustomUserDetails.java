package com.zerobase.fintech.security;


import com.zerobase.fintech.domain.entity.Member;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


public record CustomUserDetails(Member member) implements UserDetails {
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> member.getRole().toString());
  }

  @Override
  public String getPassword() {
    return member.getPassword();
  }

  @Override
  public String getUsername() {
    return member.getUsername();
  }
}
