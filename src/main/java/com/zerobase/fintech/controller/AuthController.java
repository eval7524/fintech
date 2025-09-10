package com.zerobase.fintech.controller;

import com.zerobase.fintech.domain.dto.auth.SignUpRequest;
import com.zerobase.fintech.domain.dto.auth.SignUpResponse;
import com.zerobase.fintech.domain.dto.login.LoginRequest;
import com.zerobase.fintech.domain.dto.login.LoginResponse;
import com.zerobase.fintech.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
    SignUpResponse response = authService.register(request);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
    Object response = authService.login(request, session);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpSession session) {
    if(session == null || session.getAttribute("username") == null) {
      return ResponseEntity.ok("이미 로그아웃 상태입니다.");
    }
    authService.logout(session);
    return ResponseEntity.ok("로그아웃 완료");
  }
}
