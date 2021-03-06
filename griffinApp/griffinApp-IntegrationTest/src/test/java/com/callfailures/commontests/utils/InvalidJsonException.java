package com.callfailures.commontests.utils;

import javax.ejb.ApplicationException;

@ApplicationException
public class InvalidJsonException extends RuntimeException {
	private static final long serialVersionUID = 6087454351913028554L;

	public InvalidJsonException(final String message) {
		super(message);
	}

	public InvalidJsonException(final Throwable throwable) {
		super(throwable);
	}

}