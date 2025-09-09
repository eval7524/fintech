package com.zerobase.fintech.service;


import com.zerobase.fintech.domain.dto.auth.SignUpRequest;
import com.zerobase.fintech.domain.dto.auth.SignUpResponse;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.entity.Role;
import com.zerobase.fintech.domain.repository.MemberRepository;
import com.zerobase.fintech.exception.PhoneNumberAlreadyUsedException;
import com.zerobase.fintech.exception.UserAlreadyExistsException;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public SignUpResponse register(SignUpRequest request) {
    log.info("회원가입 서비스 호출 : username = {}", request.getUsername());
    if(memberRepository.findByUsername(request.getUsername()).isPresent()) {
      log.warn("아이디명 = {} 은 이미 사용중인 아이디입니다.", request.getUsername());
      throw new UserAlreadyExistsException();
    }

    if(memberRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
      log.warn("전화번호 = {} 은 이미 사용중인 전화번호입니다.", request.getPhoneNumber());
      throw new PhoneNumberAlreadyUsedException();
    }
    String encodedPassword = passwordEncoder.encode(request.getPassword());

    Member member = Member.builder()
        .username(request.getUsername())
        .password(encodedPassword)
        .name(request.getName())
        .phoneNumber(request.getPhoneNumber())
        .role(Role.USER)
        .build();

    log.debug("회원 저장 전 상태 : {}", member);
    Member savedMember = memberRepository.save(member);
    log.info("회원 가입 완료 : memberId = {}, username = {}", savedMember.getId(), savedMember.getUsername());

    return new SignUpResponse(savedMember.getId(), savedMember.getUsername(), "회원가입이 완료되었습니다.");
  }
}
