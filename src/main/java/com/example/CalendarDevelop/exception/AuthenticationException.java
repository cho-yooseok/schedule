package com.example.CalendarDevelop.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends ApplicationException {

  public AuthenticationException(String message) {
    super(message, HttpStatus.UNAUTHORIZED);
  }
}