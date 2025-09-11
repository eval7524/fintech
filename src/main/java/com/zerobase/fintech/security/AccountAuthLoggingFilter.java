package com.zerobase.fintech.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AccountAuthLoggingFilter extends OncePerRequestFilter {

  private final Logger log = LoggerFactory.getLogger(AccountAuthLoggingFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String uri = request.getRequestURI();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if(authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
      if(uri.startsWith("/api/accounts")) {
        if(uri.equals("/api/accounts")) {
          log.warn("비로그인 사용자 계좌 조회 시도 : uri = {}", uri);
        } else if (uri.equals("/api/accounts/create")) {
          log.warn("비로그인 사용자 계좌 생성 시도 : uri = {}", uri);
        } else {
          log.warn("비로그인 사용자 계좌 접근 시도 : uri = {}, ", uri);
        }

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("{\"error\":\"인증 필요: 로그인하세요.\"}");
        return;
      }
    }
    filterChain.doFilter(request, response);
  }
}
