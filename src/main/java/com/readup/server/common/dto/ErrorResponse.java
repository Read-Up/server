package com.readup.server.common.dto;

import com.readup.server.common.exception.ErrorCode;

public record ErrorResponse(boolean success, int status, String error, String message) {
	public ErrorResponse(ErrorCode errorCode) {
		this(false, errorCode.getHttpStatus().value(), errorCode.name(), errorCode.getMessage());
	}

	public ErrorResponse(ErrorCode errorCode, String customMessage) {
		this(false, errorCode.getHttpStatus().value(), errorCode.name(), customMessage);
	}
}
