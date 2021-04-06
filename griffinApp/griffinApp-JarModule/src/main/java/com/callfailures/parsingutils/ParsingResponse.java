package com.callfailures.parsingutils;

import java.util.ArrayList;
import java.util.Collection;

public class ParsingResponse<T> {
	private  Collection<InvalidRow> invalidRows = new ArrayList<>();
	private  Collection<T> validObjects = new ArrayList<>();
		
	
	public void setInvalidRows(final Collection<InvalidRow> invalidRows) {
		this.invalidRows = invalidRows;
	}
	
	public void setValidRows(final Collection<T> validObjects) {
		this.validObjects = validObjects;
	}
	
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
