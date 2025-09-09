package com.zerobase.fintech.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleException(Exception e) {
    log.error("예외 발생 : {}", e);
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", "Internal Server Error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    log.error("회원가입 중 예외 발생 : {}", e);
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(PhoneNumberAlreadyUsedException.class)
  public ResponseEntity<Map<String, String>> handlePhoneNumberAlreadyUsedException(PhoneNumberAlreadyUsedException e) {
    log.error("전화번호 중복 예외 발생 : {}", e);
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handlerValidationException(MethodArgumentNotValidException e) {
    Map<String, String> errors = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error ->
        errors.put(error.getField(), error.getDefaultMessage())
    );

    errors.forEach((field, message) ->
        log.warn("DTO 검증 실패 - {} : {}", field, message));

    return ResponseEntity.badRequest().body(errors);
  }
}
