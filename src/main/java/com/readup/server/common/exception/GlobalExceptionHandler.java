package com.readup.server.common.exception;

import static com.readup.server.common.exception.ErrorCode.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.readup.server.common.dto.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ControllerException.class)
	public ResponseEntity<ErrorResponse> handleControllerException(ControllerException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "ControllerException");
	}

	@ExceptionHandler(ServiceException.class)
	public ResponseEntity<ErrorResponse> handleServiceException(ServiceException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "ServiceException");
	}

	@ExceptionHandler(RepositoryException.class)
	public ResponseEntity<ErrorResponse> handleRepositoryException(RepositoryException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "RepositoryException");
	}

	@ExceptionHandler(DomainException.class)
	public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "DomainException");
	}

	@ExceptionHandler(FeignException.class)
	public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
		return buildErrorResponse(ex, ex.getErrorCode(), "FeignException");
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
		String errorMessage = ex.getAllErrors().getFirst().getDefaultMessage();
		return buildErrorResponse(INVALID_PARAMETER, "HandlerMethodValidationException", errorMessage);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
		return buildErrorResponse(INVALID_PARAMETER, "MethodArgumentNotValidException", errorMessage);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException ex) {
		return buildErrorResponse(INVALID_PARAMETER, "MethodArgumentTypeMismatchException", ex.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
		MissingServletRequestParameterException ex) {
		return buildErrorResponse(MISSING_PARAMETER, "MissingServletRequestParameterException", ex.getMessage());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		log.error("[Unexpected Exception] {}", ex.getMessage(), ex);
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, ErrorCode errorCode, String exceptionType) {
		return buildErrorResponse(errorCode, exceptionType, ex.getMessage());
	}

	private ResponseEntity<ErrorResponse> buildErrorResponse(ErrorCode errorCode, String exceptionType,
		String message) {
		log.error("[{}] {} - {}", exceptionType, errorCode, message);
		ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
		return new ResponseEntity<>(errorResponse, errorCode.getHttpStatus());
	}
}
