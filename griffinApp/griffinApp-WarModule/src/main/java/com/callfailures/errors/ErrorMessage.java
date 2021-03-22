package com.callfailures.errors;

public enum ErrorMessage {
	INVALID_IMSI("The IMSI provided is invalid"),
	INVALID_PHONE_MODEL("The phone model provided is invalid"),
	INVALID_DATE("The dates provided are invalid");
	
	private final String message;

	ErrorMessage(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
