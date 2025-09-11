package com.zerobase.fintech.service;

import static org.junit.jupiter.api.Assertions.*;

import com.zerobase.fintech.domain.dto.auth.SignUpRequest;
import com.zerobase.fintech.domain.dto.auth.SignUpResponse;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private AuthService authService;

  @Test
  void registerTest() {
    SignUpRequest request = new SignUpRequest();
    request.setUsername("testUsername");
    request.setPassword("password");
    request.setName("홍길동");
    request.setPhoneNumber("010-1234-5678");

    Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
    Mockito.when(memberRepository.save(Mockito.any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

    SignUpResponse signUpResponse = authService.register(request);

    assertNotNull(signUpResponse);
    assertEquals(request.getUsername(), signUpResponse.getUsername());
    assertEquals("encodedPassword", passwordEncoder.encode(request.getPassword()));
  }
}
