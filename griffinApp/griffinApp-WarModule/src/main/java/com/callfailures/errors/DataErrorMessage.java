package com.callfailures.errors;

public class DataErrorMessage {
	private final int rowNumber;
	private final String message;

	/**
	 * Creates an instance of data error message that describes why a parsed data is invalid
	 * @param rowNumber is the erroneous/invalid data
	 * @param message is the reason why the row number is invalid
	 */
	public DataErrorMessage(final int rowNumber, final String message) {
		this.rowNumber = rowNumber;
		this.message = message;
	}

	/**
	 * 
	 * @return the row number starting from 1 of invalid data
	 */
	public int getRowNumber() {
		return rowNumber;
	}

	/**
	 * 
	 * @return the reason why the row number is invalid
	 */
	public String getMessage() {
		return message;
	}
	
}
