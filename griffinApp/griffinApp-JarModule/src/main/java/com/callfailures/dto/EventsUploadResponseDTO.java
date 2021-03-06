package com.callfailures.dto;

import java.io.Serializable;
import java.util.Collection;

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


	public EventsUploadResponseDTO(final String tabName, final int validRowCount, final Collection<InvalidRow> erroneousData) {
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
