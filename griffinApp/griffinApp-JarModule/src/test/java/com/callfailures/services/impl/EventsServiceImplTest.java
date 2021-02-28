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
import java.util.Iterator;

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
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.EventService;
import com.callfailures.services.ValidationService;

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
	void testSuccessfuleReadOfFile() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		when(marketOperatorDAO.getMarketOperator(marketOperatorPK)).thenReturn(marketOperator);
		file = new File(absolutePath + "/importData/validData.xlsx");
		eventService.read(file);
		ParsingResponse<Events> parsingResults = eventService.read(file);
		assertEquals(4, parsingResults.getValidObjects().size());
		assertEquals(0, parsingResults.getInvalidRows().size());
	}
	
	@Test
	void testInvalidDate() {
		file = new File(absolutePath + "/importData/invalidDate.xlsx");
		assertInvalidRowMessage("Invalid Date");
	}
	
	@Test
	void testInvalidCellID() {
		file = new File(absolutePath + "/importData/invalidCellId.xlsx");
		assertInvalidRowMessage("Invalid Cell ID");
	}
	
	@Test
	void testInvalidDuration() {
		file = new File(absolutePath + "/importData/invalidDuration.xlsx");
		assertInvalidRowMessage("Invalid Duration");
	}
	
	@Test
	void testInvalidNEVersion() {
		file = new File(absolutePath + "/importData/invalidNEVersion.xlsx");
		assertInvalidRowMessage("Invalid NE Version");
	}
	
	@Test
	void testInvalidIMSI() {
		file = new File(absolutePath + "/importData/invalidIMSI.xlsx");
		assertInvalidRowMessage("Invalid IMSI");
	}

	@Test
	void testInvalidHIER3_ID() {
		file = new File(absolutePath + "/importData/invalidhier3Id.xlsx");
		assertInvalidRowMessage("Invalid hier3Id");
	}
	
	@Test
	void testInvalidHIER32_ID() {
		file = new File(absolutePath + "/importData/invalidhier32Id.xlsx");
		assertInvalidRowMessage("Invalid hier32Id");
	}
	
	@Test
	void testInvalidHIER321_ID() {
		file = new File(absolutePath + "/importData/invalidhier321Id.xlsx");
		assertInvalidRowMessage("Invalid hier321Id");
	}
	
	
	@Test
	void testInvalidEventCause() {
		file = new File(absolutePath + "/importData/invalidEventCause.xlsx");
		assertInvalidRowMessage("Invalid Event and Cause Code combination");
	}
	

	@Test
	void testInexistentEventCause() {
		file = new File(absolutePath + "/importData/inexistentEventCause.xlsx");
		assertInvalidRowMessage("Inexistent Event and Cause Code combination");
	}
	
	
	@Test
	void testInvalidFailureClass() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		file = new File(absolutePath + "/importData/invalidFailureClass.xlsx");
		assertInvalidRowMessage("Invalid Failure Class Id");
	}
	
	@Test
	void testInexistentFailureClass() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		file = new File(absolutePath + "/importData/inexistentFailureClass.xlsx");
		assertInvalidRowMessage("Inexistent Failure Class Id");
	}
	
	
	@Test
	void testInvalidUEType() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		file = new File(absolutePath + "/importData/invalidFailureClass.xlsx");
		assertInvalidRowMessage("Invalid Failure Class Id");
	}
	
	@Test
	void testInexistentUEType() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		file = new File(absolutePath + "/importData/inexistentUEType.xlsx");
		assertInvalidRowMessage("Inexistent UE type");
	}
	
	
	@Test
	void testInvalidMarketOperator() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		file = new File(absolutePath + "/importData/invalidMarketOperator.xlsx");
		assertInvalidRowMessage("Invalid  MCC and MNC combination");
	}
	
	
	@Test
	void testInexistentMarketOperator() {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		file = new File(absolutePath + "/importData/inexistentMarketOperator.xlsx");
		assertInvalidRowMessage("Inexistent MCC and MNC combination");
	}
	
	private void assertInvalidRowMessage(String invalidRowMessage) {
		ParsingResponse<Events> parsingResults = eventService.read(file);
		assertEquals(0, parsingResults.getValidObjects().size());
		assertEquals(1, parsingResults.getInvalidRows().size());
		Iterator<InvalidRow> eventsIterator = parsingResults.getInvalidRows().iterator();
		assertEquals(invalidRowMessage, eventsIterator.next().getErrorMessage());
	}
}