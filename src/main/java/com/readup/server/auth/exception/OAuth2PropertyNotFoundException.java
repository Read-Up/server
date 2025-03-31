package com.readup.server.auth.exception;

import com.readup.server.common.exception.ErrorCode;
import com.readup.server.common.exception.ServiceException;

public class OAuth2PropertyNotFoundException extends ServiceException {
	public OAuth2PropertyNotFoundException(String message) {
		super(ErrorCode.PROPERTY_NOT_FOUND, message);
	}
}
