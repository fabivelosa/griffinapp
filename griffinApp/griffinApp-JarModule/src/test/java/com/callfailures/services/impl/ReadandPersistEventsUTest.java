package com.callfailures.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Paths;

import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.callfailures.dao.EventCauseDAO;
import com.callfailures.dao.EventDAO;
import com.callfailures.dao.FailureClassDAO;
import com.callfailures.dao.MarketOperatorDAO;
import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.services.EventService;
import com.callfailures.services.ValidationService;

@Disabled
class ReadandPersisteEventsUTest {
	private final EventCauseDAO eventCauseDAO = mock(EventCauseDAO.class);
	private final FailureClassDAO failureClassDAO = mock(FailureClassDAO.class);
	private final UserEquipmentDAO userEquipmentDAO = mock(UserEquipmentDAO.class);
	private final MarketOperatorDAO marketOperatorDAO = mock(MarketOperatorDAO.class);
	private final EventDAO eventDAO = mock(EventDAO.class);
	private final String absolutePath = Paths.get("src","test","resources").toFile().getAbsolutePath();
	private final EventCausePK eventCausePK = new EventCausePK(4098, 1);
	private final EventCause eventCause = new EventCause(eventCausePK, "S1 SIG CONN SETUP-S1 INTERFACE DOWN");
	private final FailureClass failureClass = new FailureClass(1,"HIGH PRIORITY ACCESS");
	private final UserEquipment userEquipment = new UserEquipment(21060800);
	private final MarketOperatorPK marketOperatorPK = new MarketOperatorPK(344,930);
	private final MarketOperator marketOperator = new MarketOperator(marketOperatorPK, "Antigua and Barbuda", "AT&T Wireless-Antigua AG");
	private Validator validator;
	private ValidationService validationService;

	private EventService eventService;
	private File file;
	
	@BeforeEach
	public void setUp() {				
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		validationService = new ValidationServiceImpl();
		((ValidationServiceImpl)validationService).validator = validator;
		((ValidationServiceImpl) validationService).eventCauseDAO = eventCauseDAO;
		((ValidationServiceImpl) validationService).failureClassDAO = failureClassDAO;
		((ValidationServiceImpl) validationService).userEquipmentDAO = userEquipmentDAO;
		((ValidationServiceImpl) validationService).marketOperatorDAO = marketOperatorDAO;
		
		eventService = new EventServiceImpl();
		((EventServiceImpl) eventService).eventDAO = eventDAO;
		((EventServiceImpl) eventService).validationService = validationService;
	}
	
	@Test
	@Disabled
	void testSuccessfuleReadOfFile() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		when(marketOperatorDAO.getMarketOperator(marketOperatorPK)).thenReturn(marketOperator);
		file = new File(absolutePath + "/importData/validData.xlsx");
		eventService.read(file);
	}
	
	@Test
	@Disabled
	void testFailedValidationService() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		when(marketOperatorDAO.getMarketOperator(marketOperatorPK)).thenReturn(marketOperator);
		file = new File(absolutePath + "/importData/validData.xlsx");
		doThrow(new FieldNotValidException("imsi","Invalid IMSI")).when(validationService).validate(anyObject());
		Throwable exception = assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid IMSI", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidDate() {
		file = new File(absolutePath + "/importData/invalidDate.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid Date", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidCellID() {
		file = new File(absolutePath + "/importData/invalidCellId.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid Cell ID", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidDuration() {
		file = new File(absolutePath + "/importData/invalidDuration.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid Duration", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidNEVersion() {
		file = new File(absolutePath + "/importData/invalidNEVersion.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid NE Version", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidIMSI() {
		file = new File(absolutePath + "/importData/invalidIMSI.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid IMSI", exception.getMessage());
	}

	@Test
	@Disabled
	void testInvalidHIER3_ID() {
		file = new File(absolutePath + "/importData/invalidhier3Id.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid hier3Id", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidHIER32_ID() {
		file = new File(absolutePath + "/importData/invalidhier32Id.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid hier32Id", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInvalidHIER321_ID() {
		file = new File(absolutePath + "/importData/invalidhier321Id.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid hier321Id", exception.getMessage());
	}
	
	
	@Test
	@Disabled
	void testInvalidEventCause() {
		file = new File(absolutePath + "/importData/invalidEventCause.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid Event and Cause Code combination", exception.getMessage());
	}
	

	@Test
	@Disabled
	void testInexistentEventCause() {
		file = new File(absolutePath + "/importData/inexistentEventCause.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Inexistent Event and Cause Code combination", exception.getMessage());
	}
	
	
	@Test
	@Disabled
	void testInvalidFailureClass() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		file = new File(absolutePath + "/importData/invalidFailureClass.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid Failure Class Id", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInexistentFailureClass() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		file = new File(absolutePath + "/importData/inexistentFailureClass.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Inexistent Failure Class Id", exception.getMessage());
	}
	
	
	@Test
	@Disabled
	void testInvalidUEType() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		file = new File(absolutePath + "/importData/invalidFailureClass.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid Failure Class Id", exception.getMessage());
	}
	
	@Test
	@Disabled
	void testInexistentUEType() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		file = new File(absolutePath + "/importData/inexistentUEType.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Inexistent UE type", exception.getMessage());
	}
	
	
	@Test
	@Disabled
	void testInvalidMarketOperator() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		file = new File(absolutePath + "/importData/invalidMarketOperator.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Invalid  MCC and MNC combination", exception.getMessage());
	}
	
	
	@Test
	@Disabled
	void testInexistentMarketOperator() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		file = new File(absolutePath + "/importData/inexistentMarketOperator.xlsx");
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> eventService.read(file));
		assertEquals("Inexistent MCC and MNC combination", exception.getMessage());
	}
}
