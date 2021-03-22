package com.callfailures.parsingutils;

import java.io.Serializable;

public class InvalidRow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6781738419227065827L;
	
	private final int rowNumber;
	private final String errorMessage;
	
	public InvalidRow(final int rowNumber, final String errorMessage) {
		this.rowNumber = rowNumber;
		this.errorMessage = errorMessage;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
