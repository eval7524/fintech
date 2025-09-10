package com.zerobase.fintech.service;

import com.zerobase.fintech.domain.dto.account.AccountRequest;
import com.zerobase.fintech.domain.dto.account.AccountResponse;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.repository.AccountRepository;
import com.zerobase.fintech.domain.repository.MemberRepository;
import com.zerobase.fintech.exception.UserNotLoggedInException;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

  private final AccountRepository accountRepository;
  private final AuthService authService;

  //로그인 유무 확인
  public Member getCurrentMember() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      throw new UserNotLoggedInException();
    }


  }
  //계좌 생성
  public AccountResponse createAccount(AccountRequest request) {
  }


}
