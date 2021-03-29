package com.callfailures.services;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.entity.Upload;
import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.entity.views.UniqueIMSI;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventService {

	
	
	/**
	 * Validates the IMSISUmmary request parameters and retrieves IMSISUmmary object from DAO layer
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
	 * Validates the PhoneModelSUmmary request parameters and retrieves PhoneModelSUmmary object from DAO layer
	 * @param ueType
	 * @param startTime
	 * @param endTime
	 * @return the PhoneModel summary, returns <b> null </b> if the parameter is invalid or no record is found
	 */
	PhoneModelSummary findCallFailuresCountByPhoneModelAndDate(final String model, final LocalDateTime startTime, final LocalDateTime endTime);

	/**
	 * Returns the list of PhoneFailures
	 * @param tac - the phone model's unique identifier
	 * @return a collection of PhoneFailures which contains the unique cause code and event id combinations including their respective counts
	 */
	List<PhoneFailures> findUniqueEventCauseCountByPhoneModel(final int tac);
	
	
	/**
	 * Validates all the fields of Base Data tab in the excel file 
	 * @param workbookFile
	 * @return and returns the validation result, ParsingResponse,  for the Base Data tab
	 */
	ParsingResponse<Events> read(File workbookFile, Upload currentUpload);
	
	/**
	 * Query Database for all IMSI with failures between Start and End time submitted
	 * @param startTime (inclusive) - the start of the period
	 * @param endTime (inclusive) - the end of the period
	 * @return list of IMSI for given time period
	 */
	List<UniqueIMSI> findIMSISBetweenDates(final LocalDateTime startTime, final LocalDateTime endTime);

	
	/**
	 * Query unique Cause Code per imsi
	 * @param imsi
	 * @return the unique event cause per IMSI
	 */
	List<Integer> findUniqueCauseCode(final String imsi);

	
	List<IMSIEvent> findFailuresByImsi(final String imsi);

	List<UniqueIMSI> findIMSIS();
	
}
