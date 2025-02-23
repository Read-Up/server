package com.readup.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.readup.server.common.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<ErrorResponse> handleControllerException(ControllerException ex) {
		log.error("[ControllerException] {} - {}", ex.getErrorCode(), ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
		log.error("[ServiceException] {} - {}", ex.getErrorCode(), ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
	}

	@ExceptionHandler(RepositoryException.class)
	public ResponseEntity<ErrorResponse> handleRepositoryException(RepositoryException ex) {
		log.error("[RepositoryException] {} - {}", ex.getErrorCode(), ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, ex.getErrorCode().getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		log.error("[Unexpected Exception] {} - {}", ex, ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
