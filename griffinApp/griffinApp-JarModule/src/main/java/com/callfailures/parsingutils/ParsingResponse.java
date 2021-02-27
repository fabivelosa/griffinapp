package com.callfailures.parsingutils;

import java.util.ArrayList;
import java.util.Collection;

public class ParsingResponse<T> {
	private final Collection<InvalidRow> invalidRows = new ArrayList<>();
	private final Collection<T> validObjects = new ArrayList<>();
	
	public ParsingResponse() {}

	
	public Collection<InvalidRow> getInvalidRows() {
		return invalidRows;
	}

	public Collection<T> getValidObjects() {
		return validObjects;
	}
	
	public void addInvalidRow(InvalidRow row) {
		invalidRows.add(row);
	}
	
	public void addValidObject(T object) {
		validObjects.add(object);
	}	
}
