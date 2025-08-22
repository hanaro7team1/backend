package com.sido.backend.security;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomControllerAdvice {
	@ExceptionHandler(CustomJwtException.class)
	protected ResponseEntity<?> handleJwtException(CustomJwtException e) {
		String message = e.getMessage();
		System.out.println("message = " + message);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", message));
	}
}
