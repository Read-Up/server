package com.readup.server.auth.exception;

import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.ServiceException;

public class UnsupportedOAuthProviderException extends ServiceException {

	public UnsupportedOAuthProviderException(String message) {
		super(ErrorCode.INVALID_REQUEST, message);
	}
}
