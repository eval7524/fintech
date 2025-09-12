package com.zerobase.fintech.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final Logger log = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
      throws IOException, ServletException {

    String uri = request.getRequestURI();
    String method = request.getMethod();

    if (uri.startsWith("/api/accounts")) {
      if(method == "POST") {
        log.warn("비로그인 사용자 계좌 생성 시도 : uri = {}, method = {}", uri, method);
      } else if (method == "GET") {
        log.warn("비로그인 사용자 계좌 조회 시도 : uri = {}, method = {}", uri, method);
      } else {
        log.warn("비로그인 사용자 알 수 없는 요청 시도 : uri = {}, method = {}", uri, method);
      }
    }

    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("{\"error\":\"인증 필요: 로그인하세요.\"}");
  }
}
