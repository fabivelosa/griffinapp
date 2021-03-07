package com.callfailures.parsingutils;

import java.util.ArrayList;
import java.util.Collection;

public class ParsingResponse<T> {
	private final Collection<InvalidRow> invalidRows = new ArrayList<>();
	private final Collection<T> validObjects = new ArrayList<>();
		
	public Collection<InvalidRow> getInvalidRows() {
		return invalidRows;
	}

	public Collection<T> getValidObjects() {
		return validObjects;
	}
	
	public void addInvalidRow(final InvalidRow row) {
		invalidRows.add(row);
	}
	
	public void addValidObject(final T object) {
		validObjects.add(object);
	}	
}
