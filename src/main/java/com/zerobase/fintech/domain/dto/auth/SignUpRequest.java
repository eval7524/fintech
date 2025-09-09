package com.zerobase.fintech.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

  @NotBlank(message = "아이디는 필수 입력입니다.")
  private String username;

  @NotBlank(message = "비밀번호는 필수 입력입니다.")
  @Size(min = 8, message = "비밀번호는 최소 8자리 이상이어야 합니다.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).+$",
      message = "비밀번호는 최소 8자리 이상이어야 하며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다."
  )
  private String password;

  @NotBlank(message = "이름은 필수 입력입니다.")
  private String name;

  @NotBlank(message = "전화번호는 필수 입력입니다.")
  @Pattern(regexp = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$", message = "전화번호 형식은 010-1234-5678 형식 이어야 합니다.")
  private String phoneNumber;
}
