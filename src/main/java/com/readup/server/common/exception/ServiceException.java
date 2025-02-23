package com.readup.server.common.exception;

public class ServiceException extends ApplicationException {
	
	public ServiceException(ErrorCode errorCode) {
		super(errorCode);
	}

	public ServiceException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
