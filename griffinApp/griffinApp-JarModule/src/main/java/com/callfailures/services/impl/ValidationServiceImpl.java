package com.callfailures.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.poi.ss.usermodel.Row;

import com.callfailures.dao.EventCauseDAO;
import com.callfailures.dao.FailureClassDAO;
import com.callfailures.dao.MarketOperatorDAO;
import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.services.ValidationService;

@Stateless
public class ValidationServiceImpl implements ValidationService{

	@Inject
	Validator validator;
	
	@Inject
	EventCauseDAO eventCauseDAO;
	
	@Inject
	FailureClassDAO failureClassDAO;
	
	@Inject
	UserEquipmentDAO userEquipmentDAO;
	
	@Inject
	MarketOperatorDAO marketOperatorDAO;
	
	
	@Override
	public void validate(final Events events) throws FieldNotValidException{
		validateFields(events);	
	}

	
	private <T> void validateFields(T object) {
		final Set<ConstraintViolation<T>> errors = validator.validate(object);
		
		final Iterator<ConstraintViolation<T>> errorsIterator = errors.iterator();
		
		if (errorsIterator.hasNext()) {
			final ConstraintViolation<T> violation = errorsIterator.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString().toLowerCase(), violation.getMessage());
		}
	}

	@Override
	public MarketOperator checkExistingMarketOperator(final Row row, final int mCcColNumber, final int mNcColNumber) {
		int market;
		int operator;
		try {
			market = (int) row.getCell(mCcColNumber).getNumericCellValue();
			operator = (int) row.getCell(mNcColNumber).getNumericCellValue();
		}catch(Exception exception) {
			throw new FieldNotValidException("marketOperator", "Invalid  MCC and MNC combination");
		}
		MarketOperator marketOperator = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(market, operator));
		if(marketOperator == null) {
			throw new FieldNotValidException("marketOperator", "Inexistent MCC and MNC combination");
		}
		return marketOperator;
	}

	@Override
	public UserEquipment checkExistingUserEquipmentType(final Row row, final int colNumber) {
		int ueID; 
		try {
			ueID = (int) row.getCell(colNumber).getNumericCellValue();
		}catch(Exception exception) {
			throw new FieldNotValidException("ueType", "Invalid UE type");
		}
		UserEquipment userEquipment = userEquipmentDAO.getUserEquipment(ueID);
		if(userEquipment == null) {
			throw new FieldNotValidException("ueType", "Inexistent UE type");
		}
		return userEquipment;
	}

	@Override
	public FailureClass checkExistingFailureClass(final Row row, final int colNumber) {
		int failureClassId;
		try {
			failureClassId = (int) row.getCell(colNumber).getNumericCellValue();
		}catch(Exception exception){
			throw new FieldNotValidException("failureClass", "Invalid Failure Class Id");
		}
		final FailureClass failureClass = failureClassDAO.getFailureClass(failureClassId);
		if(failureClass == null) {
			throw new FieldNotValidException("failureClass", "Inexistent Failure Class Id");
		}
		return failureClass;
	}

	@Override
	public EventCause checkExistingEventCause(final Row row, final int eventColumnNumber, final int causeColumnNumber) {
		int eventId;
		int causeCode;
		try {
			eventId = (int) row.getCell(eventColumnNumber).getNumericCellValue();
			causeCode = (int) row.getCell(causeColumnNumber).getNumericCellValue();
		}catch(Exception exception) {
			throw new FieldNotValidException("eventCause", "Invalid Event and Cause Code combination");
		}
		EventCause eventCause = eventCauseDAO.getEventCause(new EventCausePK(eventId, causeCode));
		if(eventCause == null) {
			throw new FieldNotValidException("eventCause", "Inexistent Event and Cause Code combination");
		}
		return eventCause;
	}

	@Override
	public LocalDateTime checkDate(final Row row, final int colNumber) {
		LocalDateTime dateTime;
		try {
			dateTime = row.getCell(colNumber).getLocalDateTimeCellValue();
		}catch(Exception exception) {
			throw new FieldNotValidException("dateTime", "Invalid Date");
		}
		return dateTime;
	}

	@Override
	public int checkCellId(final Row row, final int colNumber) {
		int cellid;
		try {
			cellid = (int) row.getCell(colNumber).getNumericCellValue();
		}catch(Exception exception) {
			throw new FieldNotValidException("cellId", "Invalid Cell ID");
		}
		return cellid;
	}

	@Override
	public int checkDuration(final Row row, final int colNumber) {
		int duration;
		try {
			duration = (int) row.getCell(colNumber).getNumericCellValue();
		} catch(Exception exception) {
			throw new FieldNotValidException("duration", "Invalid Duration");
		}
		return duration;
	}

	@Override
	public String checkNEVersion(final Row row, final int colNumber) {
		String neVersion;
		try {
			neVersion = row.getCell(colNumber).getStringCellValue();
						
			if(neVersion.toLowerCase().contains("null")) {
				throw new FieldNotValidException("neVersion", "Invalid NE Version");
			}
		}catch(Exception exception) {
			throw new FieldNotValidException("neVersion", "Invalid NE Version");
		}
		return neVersion;
	}

	@Override
	public String checkIMSI(final Row row, final int colNumber) {
		String imsi;
		try {
			imsi = BigDecimal.valueOf(row.getCell(colNumber).getNumericCellValue()).toBigInteger().toString();
			if(imsi.toLowerCase().contains("null")) {
				throw new FieldNotValidException("imsi", "Invalid IMSI");
			}			
		} catch(Exception exception) {
			throw new FieldNotValidException("IMSI", "Invalid IMSI");
		}
		return imsi;
	}

	@Override
	public String checkhier3Id(final Row row, final int colNumber) {
		String imsi;
		try {
			imsi = BigDecimal.valueOf(row.getCell(colNumber).getNumericCellValue()).toBigInteger().toString();
			
			if(imsi.toLowerCase().contains("null")) {
				throw new FieldNotValidException("hier3Id", "Invalid hier3Id");
			}			
		} catch(Exception exception) {
			throw new FieldNotValidException("hier3Id", "Invalid hier3Id");
		}
		return imsi;
	}

	@Override
	public String checkhier32Id(final Row row, final int colNumber) {
		String imsi;
		try {
			imsi = BigDecimal.valueOf(row.getCell(colNumber).getNumericCellValue()).toBigInteger().toString();
			
			if(imsi.toLowerCase().contains("null")) {
				throw new FieldNotValidException("hier32Id", "Invalid hier32Id");
			}			
		} catch(Exception exception) {
			throw new FieldNotValidException("hier32Id", "Invalid hier32Id");
		}
		return imsi;
	}

	@Override
	public String checkhier321Id(final Row row, final int colNumber) {
		String imsi;
		try {
			imsi = BigDecimal.valueOf(row.getCell(colNumber).getNumericCellValue()).toBigInteger().toString();
			if(imsi.toLowerCase().contains("null")) {
				throw new FieldNotValidException("hier321Id", "Invalid hier321Id");
			}			
		} catch(Exception exception) {
			throw new FieldNotValidException("hier321Id", "Invalid hier321Id");
		}
		return imsi;
	}

}
