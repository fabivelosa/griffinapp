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
import com.callfailures.utils.test.EntityGenerator;

public class ValidationServiceJPAFieldConstraintsUTest {

	private EntityGenerator eventsGenerator = new EntityGenerator();
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
		
		final Events events = eventsGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
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
		
		
		final Events callFailure = eventsGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
				cellId, duration, causeCode, neVersion, IMSI, hier3Id, hier32Id, hier321Id, eventCauseDescription,
				failureClassDescription, country, operator);
				
		final FieldNotValidException exception = assertThrows(FieldNotValidException.class, () -> validationService.validate(callFailure));
		
		assertEquals(invalidFieldName, exception.getInvalidFieldName());
	}

}
