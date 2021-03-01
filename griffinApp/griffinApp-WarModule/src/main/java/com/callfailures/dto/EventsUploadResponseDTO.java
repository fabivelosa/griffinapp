package com.callfailures.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.callfailures.errors.DataErrorMessage;
import com.callfailures.parsingutils.InvalidRow;

public class EventsUploadResponseDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8000035356725939818L;

	
	/**
	 * The name of the excel tab
	 */
	private final String tabName;

	/**
	 * The count of valid rows
	 */
	private final int validRowCount;
	
	
	/**
	 * The unique and ordered collection of erroneous data messages
	 */
	private final Collection<InvalidRow> erroneousData;


	public EventsUploadResponseDTO(String tabName, int validRowCount, Collection<InvalidRow> erroneousData) {
		this.tabName = tabName;
		this.validRowCount = validRowCount;
		this.erroneousData = erroneousData;
	}

	
	public String getTabName() {
		return tabName;
	}

	public int getValidRowCount() {
		return validRowCount;
	}


	public Collection<InvalidRow> getErroneousData() {
		return erroneousData;
	}
}
