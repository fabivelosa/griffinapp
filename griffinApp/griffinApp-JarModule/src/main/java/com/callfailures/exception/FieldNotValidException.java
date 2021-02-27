package com.callfailures.exception;

import javax.ejb.ApplicationException;

@ApplicationException
public class FieldNotValidException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8091254719370670655L;
	
	/**
	 * The name of the invalid field
	 */
	private final String invalidFieldName;

	public FieldNotValidException(final String invalidFieldName, final String message) {
		super(message);
		this.invalidFieldName = invalidFieldName;
	}

	public String getInvalidFieldName() {
		return invalidFieldName;
	}

	@Override
	public String toString() {
		return "FieldNotValidException [invalidFieldName=" + invalidFieldName + "]";
	}


	
	
}
