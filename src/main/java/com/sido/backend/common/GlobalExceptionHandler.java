package com.sido.backend.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sido.backend.common.dto.ErrorResponseDTO;
import com.sido.backend.common.exception.BadRequestException;
import com.sido.backend.common.exception.ConflictException;
import com.sido.backend.common.exception.ForbiddenException;
import com.sido.backend.common.exception.ResourceGoneException;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ErrorResponseDTO(ex.getMessage(), "NOT_FOUND"));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage())
		);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponseDTO(ex.getMessage(), "BAD_REQUEST"));
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponseDTO> handleConflictException(ConflictException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ErrorResponseDTO(ex.getMessage(), "CONFLICT"));
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponseDTO> handleForbiddenException(ForbiddenException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.body(new ErrorResponseDTO(ex.getMessage(), "FORBIDDEN"));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ErrorResponseDTO("데이터 충돌이 발생했습니다.", "CONFLICT"));
	}

	@ExceptionHandler(ResourceGoneException.class)
	public ResponseEntity<ErrorResponseDTO> handleResourceGoneException(Exception ex) {
		return ResponseEntity.status(HttpStatus.GONE)
			.body(new ErrorResponseDTO(ex.getMessage(), "RESOURCE_GONE"));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO(ex.getMessage(), "BAD_REQUEST");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
