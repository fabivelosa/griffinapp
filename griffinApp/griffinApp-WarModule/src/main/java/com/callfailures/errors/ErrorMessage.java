package com.callfailures.errors;

public enum ErrorMessage {
	INVALID_IMSI("The IMSI provided is invalid"),
	INVALID_DATE("The dates provided are invalid");
	
	private final String message;

	private ErrorMessage(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
