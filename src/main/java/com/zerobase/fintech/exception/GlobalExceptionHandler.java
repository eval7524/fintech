package com.zerobase.fintech.exception;

import com.zerobase.fintech.exception.AuthException.InvalidCredentialsException;
import com.zerobase.fintech.exception.AuthException.PhoneNumberAlreadyUsedException;
import com.zerobase.fintech.exception.AuthException.MemberAlreadyExistsException;
import com.zerobase.fintech.exception.AuthException.MemberNotFoundException;
import com.zerobase.fintech.exception.AuthException.MemberNotLoggedInException;
import com.zerobase.fintech.exception.TransactionException.AccountNotFoundException;
import com.zerobase.fintech.exception.TransactionException.InvalidAmountException;
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
    log.error("예외 발생 : {}", e.getMessage());
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", "Internal Server Error");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }

  @ExceptionHandler(MemberAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(MemberAlreadyExistsException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(PhoneNumberAlreadyUsedException.class)
  public ResponseEntity<Map<String, String>> handlePhoneNumberAlreadyUsedException(PhoneNumberAlreadyUsedException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handlerValidationException(MethodArgumentNotValidException e) {
    Map<String, String> body = new HashMap<>();
    e.getBindingResult().getFieldErrors().forEach(error ->
        body.put(error.getField(), error.getDefaultMessage())
    );

    body.forEach((field, message) ->
        log.warn("DTO 검증 실패 - {} : {}", field, message));

    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(MemberNotFoundException.class)
  public ResponseEntity<Map<String, String>> handlerUserNotFoundException(MemberNotFoundException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Map<String, String>> handlerInvalidCredentialsException(InvalidCredentialsException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(MemberNotLoggedInException.class)
  public ResponseEntity<Map<String, String>> handlerUserNotLoggedInException(MemberNotLoggedInException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
  }

  @ExceptionHandler(AccountNotFoundException.class)
  public ResponseEntity<Map<String, String>> handlerAccountNotFoundException(AccountNotFoundException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(InvalidAmountException.class)
  public ResponseEntity<Map<String, String>> handlerInvalidAmountException(InvalidAmountException e) {
    Map<String, String> body = new HashMap<>();
    body.put("ERROR", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }
}
