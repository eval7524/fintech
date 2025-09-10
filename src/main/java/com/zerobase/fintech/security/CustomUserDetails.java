package com.zerobase.fintech.security;


import com.zerobase.fintech.domain.entity.Member;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final Member member;
  public CustomUserDetails(Member member) {
    this.member = member;
  }
  public Member getMember() {
    return member;
  }


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
