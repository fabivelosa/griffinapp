package com.callfailures.services;

import java.time.LocalDateTime;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Row;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.UserEquipment;
import com.callfailures.exception.FieldNotValidException;

@Local
public interface ValidationService {

	/**
	 * Reads EventID and Cause Code combination from a Row and returns the existing
	 * EventCause stored in the database
	 * 
	 * @param row               - the Row object
	 * @param eventColumnNumber - the 0-index based column number of EventID
	 * @param causeColumnNumber - the 0-index based column number of Cause Code
	 * @return the existing EventCause object stored in the database
	 * @throws FieldNotValidException if (1) the valid Failure ID is not stored in
	 *                                the database OR (2) either the EventID or
	 *                                Cause Code is invalid (a String, a null or an
	 *                                empty value)
	 */
	EventCause checkExistingEventCause(Row row, int eventColumnNumber, int causeColumnNumber)
			throws FieldNotValidException;

	/**
	 * Reads Failure ID from a Row and returns the existing FailureClass stored in
	 * the database
	 * 
	 * @param row    - the Row object
	 * @param column - the 0-index based column number of Failure ID
	 * @return the existing FailureClass object stored in the database
	 * @throws FieldNotValidException if (1) the valid Failure ID is not stored in
	 *                                the database OR (2) either the Failure ID or
	 *                                Cause Code is invalid (a String, a null or an
	 *                                empty value)
	 */
	FailureClass checkExistingFailureClass(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads Failure ID from a Row and returns the existing FailureClass stored in
	 * the database
	 * 
	 * @param row    - the FailureClass object
	 * @return the existing FailureClass object stored in the database
	 * @throws FieldNotValidException if (1) the valid Failure ID already exists
	 */
	FailureClass checkExistingFailureClass(FailureClass newItem) throws FieldNotValidException;

	/**
	 * Reads TAC ID from a Row and returns the existing UserEquipment stored in the
	 * database
	 * 
	 * @param row    - the Row object
	 * @param column - the 0-index based column number of TAC ID
	 * @return the existing UserEquipment object stored in the database
	 * @throws FieldNotValidException if (1) the valid TAC ID is not yet stored in
	 *                                the database OR (2) either the TAC ID or Cause
	 *                                Code is invalid (a String, a null or an empty
	 *                                value)
	 */
	UserEquipment checkExistingUserEquipmentType(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads TAC ID from a Row and returns the existing UserEquipment stored in the
	 * database
	 * 
	 * @param row    - the Row object
	 * @param column - the 0-index based column number of TAC ID
	 * @return the existing UserEquipment object stored in the database
	 * @throws FieldNotValidException if (1) the valid TAC ID is not yet stored in
	 *                                the database OR (2) either the TAC ID or Cause
	 *                                Code is invalid (a String, a null or an empty
	 *                                value)
	 */
	UserEquipment checkExistingUserEquipmentType(UserEquipment newItem) throws FieldNotValidException;

	/**
	 * Reads MCC ID and MNC ID from a Row and returns the existing MarketOperator
	 * stored in the database
	 * 
	 * @param row             - the Row object
	 * @param mCcColumnNumber - the 0-index based column number of MCC (Mobile
	 *                        Country Code)
	 * @param mNcColumnNumber - the 0-index based column number of MCC (Mobile
	 *                        Network Code)
	 * @return the existing MarketOperator object stored in the database
	 * @throws FieldNotValidException if (1) the valid Failure ID already exists
	 */
	MarketOperator checkExistingMarketOperator(Row row, final int mCcColumnNumber, final int mNcColumnNumber)
			throws FieldNotValidException;

	/**
	 * Checks the validity of call failure fields
	 * 
	 * @param callFailure instance that will be validated
	 * @throws FieldNotValidException
	 */
	void validate(Events event) throws FieldNotValidException;

	/**
	 * Reads a LocalDateTime column
	 * 
	 * @param row    - the Row object
	 * @param column - the 0-index based column number of LocalDateTime
	 * @return the valid LocalDateTime
	 * @throws FieldNotValidException if the Date cell is empty, null, or a String
	 */
	LocalDateTime checkDate(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads a Cell ID
	 * 
	 * @param row    - the Row object
	 * @param column - the 0-index based column number of Cell ID
	 * @return the valid Cell ID
	 * @throws FieldNotValidException if the CellID cell is empty, null, or a String
	 */
	int checkCellId(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads a Duration
	 * 
	 * @param row    - the Row object
	 * @param column - 0-index based column number of Cell ID
	 * @return the valid Duration
	 * @throws FieldNotValidException if the Duration cell is empty, null, or a
	 *                                String
	 */
	int checkDuration(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads the NE Version
	 * 
	 * @param -      row the Row object
	 * @param column - 0-index based column number of NE Version
	 * @return the valid Duration
	 * @throws FieldNotValidException if the NE Version cell is empty, or null
	 */
	String checkNEVersion(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads the IMSI
	 * 
	 * @param row    - the Row object
	 * @param column - 0-index based column number of IMSI
	 * @return the valid IMSI
	 * @throws FieldNotValidException if the IMSI cell is empty, or null
	 */
	String checkIMSI(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads the HIER3_ID
	 * 
	 * @param row    - the Row object
	 * @param column - 0-index based column number of HIER3_ID
	 * @return the valid HIER3_ID
	 * @throws FieldNotValidException if the HIER3_ID cell is empty, or null
	 */
	String checkhier3Id(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads the HIER32_ID
	 * 
	 * @param row    - the Row object
	 * @param column - 0-index based column number of HIER32_ID
	 * @return the valid HIER32_ID
	 * @throws FieldNotValidException if the HIER32_ID cell is empty, or null
	 */
	String checkhier32Id(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads the HIER321_ID
	 * 
	 * @param row    - the Row object
	 * @param column - 0-index based column number of HIER321_ID
	 * @return the valid HIER321_ID
	 * @throws FieldNotValidException if the HIER321_ID cell is empty, or null
	 */
	String checkhier321Id(Row row, int column) throws FieldNotValidException;

	/**
	 * Reads the FailureClass
	 * 
	 * @param FailureClass Object
	 * @return the valid FailureClass
	 * @throws FieldNotValidException if the FailureClass already exists
	 */
	boolean checkValidFieldsFailureClass(Row row) throws FieldNotValidException;

	/**
	 * Reads the EventCause
	 * 
	 * @param EventCause Object
	 * @return the valid EventCause
	 * @throws FieldNotValidException if the EventCause already exists
	 */
	EventCause checkExistingEventCause(EventCause newItem) throws FieldNotValidException;

	/**
	 * Reads the MarketOperator
	 * 
	 * @param MarketOperator Object
	 * @return the valid MarketOperator
	 * @throws FieldNotValidException if the MarketOperator already exists
	 */
	MarketOperator checkExistingMarketOperator(MarketOperator operator) throws FieldNotValidException;

}
