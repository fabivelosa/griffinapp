package com.callfailures.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.services.ValidationService;

public class ValidationServiceImplUTest {

	private ValidationService validationService;
	private Validator validator;
	
	@BeforeEach
	public void initTest(){
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		validationService = new ValidationServiceImpl();
		((ValidationServiceImpl)validationService).validator = validator;
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/validationservice/validData.csv", numLinesToSkip = 1)
	public void testValidCallFailure(final String localDateTime, final Integer eventId, final Integer failureId, 
								final Integer ueId, final Integer mCc, final Integer mNc, final Integer cellId, final Integer duration,
								final Integer causeCode, final String neVersion, final String IMSI, final String hier3Id, 
								final String hier32Id, final String hier321Id, 
								final String eventCauseDescription, final String failureClassDescription,
								final String country, final String operator) {
		
		final Events events = generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
				cellId, duration, causeCode, neVersion, IMSI, hier3Id, hier32Id, hier321Id, eventCauseDescription,
				failureClassDescription, country, operator);
		
		validationService.validate(events);
		
	}
	
	
	
	@ParameterizedTest
	@CsvFileSource(resources = "/validationservice/invalidData.csv", numLinesToSkip = 1)
	public void testCallFailureWithInvalidField(final String invalidFieldName, final String localDateTime, final Integer eventId, final Integer failureId, 
								final Integer ueId, final Integer mCc, final Integer mNc, final Integer cellId, final Integer duration,
								final Integer causeCode, final String neVersion, final String IMSI, final String hier3Id, 
								final String hier32Id, final String hier321Id, 
								final String eventCauseDescription, final String failureClassDescription,
								final String country, final String operator) {
		
		
		final Events callFailure = generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
				cellId, duration, causeCode, neVersion, IMSI, hier3Id, hier32Id, hier321Id, eventCauseDescription,
				failureClassDescription, country, operator);
				
		final FieldNotValidException exception = assertThrows(FieldNotValidException.class, () -> validationService.validate(callFailure));
		
		assertEquals(invalidFieldName, exception.getInvalidFieldName());
	}

	private Events generateCallFailureInstance(final String localDateTime, final Integer eventId,
			final Integer failureId, final Integer ueId, final Integer mCc, final Integer mNc, final Integer cellId,
			final Integer duration, final Integer causeCode, final String neVersion, final String IMSI,
			final String hier3Id, final String hier32Id, final String hier321Id, final String eventCauseDescription,
			final String failureClassDescription, final String country, final String operator) {
		final LocalDateTime dateTime = parseDateTime(localDateTime);
		final EventCause eventCause = parseEventCause(eventId, causeCode, eventCauseDescription);
		final FailureClass failureClass = parseFailureClass(failureId, failureClassDescription);
		final MarketOperator marketOperator = parseMobileCountryNetworkCode(mCc, mNc, country, operator);
		final UserEquipment ueType = parseUE(ueId);
		
		final Events events = new Events(eventCause, dateTime, failureClass, ueType, marketOperator, cellId, duration, neVersion,
				IMSI, hier3Id, hier32Id, hier321Id);
		
		return events;
	}

	
	private UserEquipment parseUE(final Integer ueId) {
		final UserEquipment ue = ueId == null ? null : new UserEquipment(ueId);
		return ue;
	}

	
	private MarketOperator parseMobileCountryNetworkCode(final Integer mCc, final Integer mNc, final String country,
			final String operator) {
		final MarketOperator mobileCountryNetworkCode = new MarketOperator(new MarketOperatorPK(mCc, mNc), country, operator);
		return mobileCountryNetworkCode;
	}

	
	private FailureClass parseFailureClass(final Integer failureId, final String failureClassDescription) {
		final FailureClass failureClass = new FailureClass(failureId, failureClassDescription);
		return failureClass;
	}

	
	private EventCause parseEventCause(final Integer eventId, final Integer causeCode,
			final String eventCauseDescription) {
		final EventCause eventCause = (eventId == null || causeCode == null) ? null : new EventCause(new EventCausePK(eventId, causeCode), eventCauseDescription);
		return eventCause;
	}

	
	private LocalDateTime parseDateTime(final String localDateTime) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"); 
		final LocalDateTime dateTime = localDateTime == null ? null : LocalDateTime.parse(localDateTime, formatter);
		return dateTime;
	}

}
