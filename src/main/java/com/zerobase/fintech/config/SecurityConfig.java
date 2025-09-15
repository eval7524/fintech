package com.zerobase.fintech.config;

import com.zerobase.fintech.security.CustomAuthenticationEntryPoint;
import com.zerobase.fintech.security.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.FrameworkServlet;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;


  @Bean
  public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
    auth.userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());
    return auth.build();
  }


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, FrameworkServlet frameworkServlet,
      CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
    http
        // REST API는 csrf 토큰이 필요 없음 -> 비활성화
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .maximumSessions(1) // 한 계정당 동시 접속 가능한 세션 수 1
            .maxSessionsPreventsLogin(true)) // 이미 로그인된 세션이 있을 때 새로운 로그인 시도 차단
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() //회원가입, 로그인은 인증 없이 접근 허용
            .requestMatchers("/h2-console/**").permitAll() // h2-console 접근 허용
            .requestMatchers("/api/deposits").permitAll() // 무통장 입금의 경우 ATM 기기에서 인증 없이 입금 요청 가능
            .requestMatchers("/api/accounts/**").authenticated() // 계좌 CRUD 는 인증 필요
        )
        // Spring Security 가 인증이나 권한 여부 문제로 요청을 막을 때 줄 응답 정하기
        .exceptionHandling(e -> e
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler((request, response, accessDeniedException) -> {
              response.setContentType("application/json;charset=UTF-8");
              response.setStatus(HttpServletResponse.SC_FORBIDDEN);
              response.getWriter().write("{\"error\":\"권한이 없습니다.\"}");
            })
        )
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        .formLogin(form -> form.disable());

    return http.build();
  }


  // Spring Security 에서 비밀번호 암호화 하기 위해 사용, @Bean 등록 이유는 프로젝트 어디서든 @Autowired나 생성자 주입으로 사용 가능하게 하기위해서
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }



}
