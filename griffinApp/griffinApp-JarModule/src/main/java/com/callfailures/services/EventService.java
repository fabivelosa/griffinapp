package com.callfailures.services;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventService {

	
	/**
	 * Retreives from DAO a list of event ids and cause codes of failures for given IMSI
	 * @param imsi
	 * @return list of IMSI Events, returns <b> null </b> if no failures found.
	 */
	List<IMSIEvent> findFailuresByImsi(final String imsi);
	
	/**
	 * Validates the IMSISUmmary request parameters and retrieves IMSISUmmary object from DAO layes
	 * @param imsi
	 * @param startTime
	 * @param endTime
	 * @return the IMSI summary, returns <b> null </b> if the parameter is invalid or no record is found
	 * @throws InvalidIMSIException for invalid or <b>null</b> values 
	 * @throws InvalidDateException for null values
	 */
	IMSISummary findCallFailuresCountByIMSIAndDate(final String imsi, final LocalDateTime startTime, final LocalDateTime endTime)
		throws InvalidIMSIException, InvalidDateException;
	
	/**
	 * Validates all the fields of Base Data tab in the excel file 
	 * @param workbookFile
	 * @return and returns the validation result, ParsingResponse,  fof the Base Data tab
	 */
	ParsingResponse<Events> read(File workbookFile);


	
}
