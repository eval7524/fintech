package com.zerobase.fintech.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("존재하지 않는 아이디거나 비밀번호를 틀렸습니다.");
  }
}
