package com.callfailures.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Sheet;

import com.callfailures.entity.Events;
import com.callfailures.entity.Upload;
import com.callfailures.entity.views.DeviceCombination;
import com.callfailures.entity.views.IMSICount;
import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.entity.views.UniqueIMSI;
import com.callfailures.exception.InvalidDateException;
import com.callfailures.exception.InvalidIMSIException;
import com.callfailures.exception.InvalidPhoneModelException;
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
	 ParsingResponse<Events> read(final Sheet sheet, int ini, int end, final Upload currentUpload);
	
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

	List<DeviceCombination> findTopTenEvents(final LocalDateTime startTime, final LocalDateTime endTime);

	List<IMSICount> findIMSIS(int number, LocalDateTime startTime, LocalDateTime endTime);
	
	//As a Support Engineer I want to display, for a given failure Cause Class, the IMSIs that were affected.
	List<UniqueIMSI> findIMSISByFailure(final int failureClass);
	
	
	/**
	 * Drill down of all IMSI Events By Date
	 * @param imsi
	 * @param startTime
	 * @param endTime
	 * @return the list of events associated with an IMSI
	 * @throws InvalidIMSIException
	 */
	List<Events> findListofIMSIEventsByDate(final String imsi, final LocalDateTime startTime, final LocalDateTime endTime) throws InvalidIMSIException;


	/**
	 * Queries the drilldown data for a Market, Operator, Cell ID combo in a given period
	 * @param cellID
	 * @param country
	 * @param operator
	 * @return list of Events associated with the Market, Operator, Cell ID
	 * @throws InvalidIMSIException
	 */
	List<Events> findListofIMSIEventsByMarketOperatorCellID(final int cellID, final String country, final String operator);
	
	/**
	 * Drill down of all IMSI Events By Phone Model
	 * @param model
	 * @param startTime
	 * @param endTime
	 * @return the list of events associated with a Phone Model
	 * @throws InvalidPhoneModelException
	 */
	List<Events> findListofIMSIEventsByPhoneModel(final String model, final LocalDateTime startTime, final LocalDateTime endTime) throws InvalidPhoneModelException;

	List<Events> findListOfEventsByDescription(final String description);
}
