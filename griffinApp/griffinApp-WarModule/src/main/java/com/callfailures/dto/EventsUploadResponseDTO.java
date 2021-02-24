package com.callfailures.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import com.callfailures.errors.DataErrorMessage;

public class EventsUploadResponseDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8000035356725939818L;


	/**
	 * The unique and ordered collection of valid extracted data
	 */
	private final Collection<Integer> validRowNumbers = new ArrayList<>();
	
	
	/**
	 * The unique and ordered collection of erroneous data messages
	 */
	private final Collection<DataErrorMessage> erroneousData = new LinkedHashSet<>();
	
	
	/**
	 * Adds a valid call failure data
	 * @param callFailure, a validated callFailure object
	 */
	public void addValidDataRowNumber(final int rowNumber) {
		validRowNumbers.add(rowNumber);
	}
	
	
	/**
	 * Adds a data error object containing the row number of invalid data, and the error message
	 * @param dataErrorMessage
	 */
	public void addErroneousData(final DataErrorMessage dataErrorMessage) {
		erroneousData.add(dataErrorMessage);
	}

	
	/**
	 * Required call failure getter method for serialization into JSON
	 * @return
	 */
	public Collection<Integer> getValidRowNumbers() {
		return validRowNumbers;
	}

	
	/**
	 * Required erroneous data getter method for serialization into JSON
	 * @return
	 */
	public Collection<DataErrorMessage> getErroneousData() {
		return erroneousData;
	}
	

}
