package com.readup.server.common.exception;

public class RepositoryException extends ApplicationException {

	public RepositoryException(ErrorCode errorCode) {
		super(errorCode);
	}

	public RepositoryException(ErrorCode errorCode, String customMessage) {
		super(errorCode, customMessage);
	}
}
