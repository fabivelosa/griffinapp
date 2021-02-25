package com.callfailures.errors;

public enum CallFailureErrors {
	INVALID_FIELD("Invalid Field");
	
	private final String errorMessage;
	
	CallFailureErrors(final String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
