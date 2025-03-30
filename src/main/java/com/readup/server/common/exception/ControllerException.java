package com.readup.server.common.exception;

public class ControllerException extends ApplicationException {

	public ControllerException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ControllerException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
