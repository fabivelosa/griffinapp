package com.callfailures.parsingutils;

public class InvalidRow {
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
