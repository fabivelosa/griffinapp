package com.callfailures.services;

import java.io.File;
import java.time.LocalDateTime;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface EventService {

	/**
	 * Validates the IMSISUmmary request parameters and retrieves IMSISUmmary object from DAO layers
	 * @param imsi
	 * @param startTime
	 * @param endTime
	 * @return the IMSI summary, returns <b> null </b> if the parameter is invalid or no record is found
	 */
	IMSISummary findCallFailuresCountByIMSIAndDate(final String imsi, final LocalDateTime startTime, final LocalDateTime endTime);
	
	/**
	 * Validates all the fields of Base Data tab in the excel file 
	 * @param workbookFile
	 * @return and returns the validation result, ParsingResponse,  fof the Base Data tab
	 */
	ParsingResponse<Events> read(File workbookFile);

}
