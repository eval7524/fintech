package com.zerobase.fintech.service;


import com.zerobase.fintech.domain.dto.auth.SignUpRequest;
import com.zerobase.fintech.domain.dto.auth.SignUpResponse;
import com.zerobase.fintech.domain.dto.login.LoginRequest;
import com.zerobase.fintech.domain.dto.login.LoginResponse;
import com.zerobase.fintech.domain.entity.Member;
import com.zerobase.fintech.domain.entity.Role;
import com.zerobase.fintech.domain.repository.MemberRepository;
import com.zerobase.fintech.exception.InvalidCredentialsException;
import com.zerobase.fintech.exception.PhoneNumberAlreadyUsedException;
import com.zerobase.fintech.exception.UserAlreadyExistsException;
import com.zerobase.fintech.exception.UserNotFoundException;
import com.zerobase.fintech.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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

  @Transactional(readOnly = true)
  public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
    log.info("로그인 시도 : username = {}", request.getUsername());


    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    if(currentAuth != null && currentAuth.isAuthenticated()
    && !(currentAuth instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
      log.info("이미 로그인 상태입니다. username = {}", currentAuth.getName());
      return new LoginResponse("SESSION_ACTIVE", currentAuth.getName(), "ALREADY_LOGGED_IN");
    }

    Member member = memberRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new UserNotFoundException());

    if(!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
      log.warn("비밀번호 불일치 : username = {}", request.getUsername());
      throw new InvalidCredentialsException();
    }

    CustomUserDetails userDetails = new CustomUserDetails(member);

    Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    httpRequest.getSession(true).setAttribute(
        HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
        context
    );

    SecurityContextHolder.setContext(context);


    log.info("로그인 성공 : username = {}", member.getUsername());
    return new LoginResponse("SESSION_ACTIVE", member.getUsername(), "LOGIN_SUCCESS");
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      new SecurityContextLogoutHandler().logout(request, response, authentication);
      log.info("로그아웃 완료 : username = {}", authentication.getName());
    } else {
      log.info("이미 로그아웃된 상태입니다.");
    }
  }
}
