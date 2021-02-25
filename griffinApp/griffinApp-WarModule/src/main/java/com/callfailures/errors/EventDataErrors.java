package com.callfailures.errors;

public enum EventDataErrors {
	INVALID_FIELD("Invalid Field");
	
	private final String errorMessage;
	
	EventDataErrors(final String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
