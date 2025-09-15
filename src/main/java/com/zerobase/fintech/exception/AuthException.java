package com.zerobase.fintech.exception;

public class AuthException extends RuntimeException {

  public AuthException(String message) {
    super(message);
  }

  public static class MemberNotFoundException extends AuthException {

    public MemberNotFoundException() {
      super("존재하지 않는 아이디거나 비밀번호를 틀렸습니다.");
    }
  }

  public static class MemberAlreadyExistsException extends AuthException {

    public MemberAlreadyExistsException() {
      super("이미 존재하는 아이디입니다.");
    }
  }

  public static class MemberNotLoggedInException extends AuthException {

    public MemberNotLoggedInException() {
      super("로그인이 필요합니다.");
    }
  }

  public static class InvalidCredentialsException extends AuthException {

    public InvalidCredentialsException() {
      super("아이디 또는 비밀번호를 확인해주세요.");
    }
  }

  public static class PhoneNumberAlreadyUsedException extends AuthException {

    public PhoneNumberAlreadyUsedException() {
      super("이미 등록된 전화번호입니다.");
    }
  }

}

