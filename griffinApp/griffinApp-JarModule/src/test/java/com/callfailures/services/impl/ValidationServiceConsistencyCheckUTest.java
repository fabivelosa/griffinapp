package com.callfailures.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;

import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

class ValidationServiceConsistencyCheckUTest {
	private final EventCauseDAO eventCauseDAO = mock(EventCauseDAO.class);
	private final FailureClassDAO failureClassDAO = mock(FailureClassDAO.class);
	private final UserEquipmentDAO userEquipmentDAO = mock(UserEquipmentDAO.class);
	private final MarketOperatorDAO marketOperatorDAO = mock(MarketOperatorDAO.class);
	private final String absolutePath = Paths.get("src","test","resources").toFile().getAbsolutePath();
	private final EventCausePK eventCausePK = new EventCausePK(4098, 1);
	private final MarketOperatorPK marketOperatorPK = new MarketOperatorPK(515,1);
	private final MarketOperator marketOperator = new MarketOperator(marketOperatorPK, "Philippines", "Globe Telecom");
	private final EventCause eventCause = new EventCause(eventCausePK, "S1 SIG CONN SETUP-S1 INTERFACE DOWN");
	private final FailureClass failureClass = new FailureClass(1,"HIGH PRIORITY ACCESS");
	private final UserEquipment userEquipment = new UserEquipment(21060800);
	private Validator validator;
	private ValidationService validationService;

	
	@BeforeEach
	public void setUp() {				
		validator = Validation.buildDefaultValidatorFactory().getValidator();
		validationService = new ValidationServiceImpl();
		((ValidationServiceImpl)validationService).validator = validator;
		((ValidationServiceImpl) validationService).eventCauseDAO = eventCauseDAO;
		((ValidationServiceImpl) validationService).failureClassDAO = failureClassDAO;
		((ValidationServiceImpl) validationService).userEquipmentDAO = userEquipmentDAO;
		((ValidationServiceImpl) validationService).marketOperatorDAO = marketOperatorDAO;		
	}
	
	@Test
	void testInvalidDate() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidDate.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkDate(row, 0));
		assertEquals("Invalid Date", exception.getMessage());
		
	}

	@Test
	void testInvalidCellID() throws InvalidFormatException, IOException {		
		Row row = generateRow("/importData/invalidCellId.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkCellId(row, 6));
		assertEquals("Invalid Cell ID", exception.getMessage());
	}
	
	@Test
	void testInvalidDuration() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidDuration.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () ->validationService.checkDuration(row, 7));
		assertEquals("Invalid Duration", exception.getMessage());
	}
	
	@Test
	void testInvalidNEVersion() throws InvalidFormatException, IOException {
		Row row = generateRow( "/importData/invalidNEVersion.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkNEVersion(row, 9));
		assertEquals("Invalid NE Version", exception.getMessage());
	}
	
	@Test
	void testInvalidIMSI() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidIMSI.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkIMSI(row, 10));
		assertEquals("Invalid IMSI", exception.getMessage());
	}

	@Test
	void testInvalidHIER3_ID() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidhier3Id.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkhier3Id(row, 11));
		assertEquals("Invalid hier3Id", exception.getMessage());
	}
	
	@Test
	void testInvalidHIER32_ID() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidhier32Id.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkhier32Id(row, 12));
		assertEquals("Invalid hier32Id", exception.getMessage());
	}
	
	
	@Test
	void testInvalidHIER321_ID() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidhier321Id.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkhier321Id(row, 13));
		assertEquals("Invalid hier321Id", exception.getMessage());
	}
	

	
	@Test
	void testInvalidEventCause() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/invalidEventCause.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkExistingEventCause(row, 1, 2));
		assertEquals("Invalid Event and Cause Code combination", exception.getMessage());
	}
	

	@Test
	void testInexistentEventCause() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/inexistentEventCause.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkExistingEventCause(row, 1, 2));
		assertEquals("Inexistent Event and Cause Code combination", exception.getMessage());
	}
	
	
	@Test
	void testInvalidFailureClass() throws InvalidFormatException, IOException {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		Row row = generateRow("/importData/invalidFailureClass.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkExistingFailureClass(row, 2));
		assertEquals("Invalid Failure Class Id", exception.getMessage());
	}
	
	@Test
	void testInexistentFailureClass() throws InvalidFormatException, IOException {
		Row row = generateRow("/importData/inexistentFailureClass.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkExistingFailureClass(row, 2));
		assertEquals("Inexistent Failure Class Id", exception.getMessage());
	}
	
	
	@Test
	void testInvalidUEType() throws InvalidFormatException, IOException {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		Row row = generateRow("/importData/invalidUEType.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () ->validationService.checkExistingUserEquipmentType(row, 3));
		assertEquals("Invalid UE type", exception.getMessage());
	}
	
	@Test
	void testInexistentUEType() throws InvalidFormatException, IOException {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		Row row = generateRow("/importData/inexistentUEType.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () ->validationService.checkExistingUserEquipmentType(row, 3));
		assertEquals("Inexistent UE type", exception.getMessage());
	}
	
	
	@Test
	void testInvalidMarketOperator() throws InvalidFormatException, IOException {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		Row row = generateRow("/importData/invalidMarketOperator.xlsx");	
		Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkExistingMarketOperator(row, 4,5));
		assertEquals("Invalid  MCC and MNC combination", exception.getMessage());
	}
	
	
	@Test
	void testInexistentMarketOperator() throws InvalidFormatException, IOException {
		when(eventCauseDAO.getEventCause(anyObject())).thenReturn(eventCause);
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		when(userEquipmentDAO.getUserEquipment(21060800)).thenReturn(userEquipment);
		Row row = generateRow("/importData/inexistentMarketOperator.xlsx");			Throwable exception =  assertThrows(FieldNotValidException.class, () -> validationService.checkExistingMarketOperator(row, 4,5));
		assertEquals("Inexistent MCC and MNC combination", exception.getMessage());
	}
	
	@Test
	void testCheckExistingFailureClassByActualObjectNotExisting() {
		FailureClass retrievedFailureClass = validationService.checkExistingFailureClass(failureClass);
		assertNull(retrievedFailureClass);
	}
	
	@Test
	void testCheckExistingFailureClassByActualObjectExisting() {
		when(failureClassDAO.getFailureClass(anyInt())).thenReturn(failureClass);
		Throwable exception = assertThrows(FieldNotValidException.class, () -> validationService.checkExistingFailureClass(failureClass));
		assertEquals("Already exists Failure Class Id", exception.getMessage());
		assertEquals("failureClass", ((FieldNotValidException)exception).getInvalidFieldName());
	}
	
	@Test
	void testCheckExistingUserEquipmentByActualObjectNotExisting() {
		UserEquipment retrievedUserEquipment = validationService.checkExistingUserEquipmentType(userEquipment);
		assertNull(retrievedUserEquipment);
	}
	
	@Test
	void testCheckExistingUserEquipmentByActualObjectExisting() {
		when(userEquipmentDAO.getUserEquipment(anyInt())).thenReturn(userEquipment);
		Throwable exception = assertThrows(FieldNotValidException.class, () -> validationService.checkExistingUserEquipmentType(userEquipment));
		assertEquals("Already exists ue TAC", exception.getMessage());
		assertEquals("ueType", ((FieldNotValidException)exception).getInvalidFieldName());
	}
	
	@Test
	void testCheckExistingMarketOPeratorByActualObjectNotExisting() {
		MarketOperator retrievedMarketOperator = validationService.checkExistingMarketOperator(marketOperator);
		assertNull(retrievedMarketOperator);
	}
	
	@Test
	void testCheckExistingMarketOPeratorByActualObjectExisting() {
		when(marketOperatorDAO.getMarketOperator(marketOperatorPK)).thenReturn(marketOperator);
		Throwable exception = assertThrows(FieldNotValidException.class, () -> validationService.checkExistingMarketOperator(marketOperator));
		assertEquals("Already exists Market Operator", exception.getMessage());
		assertEquals("operator", ((FieldNotValidException)exception).getInvalidFieldName());
	}
	
	@Test
	void testCheckExistingEventCauseByActualObjectNotExisting() {
		EventCause retrievedEventCause = validationService.checkExistingEventCause(eventCause);
		assertNull(retrievedEventCause);
	}
	
	@Test
	void testCheckExistingEventCauseByActualObjectExisting() {
		when(eventCauseDAO.getEventCause(eventCausePK)).thenReturn(eventCause);
		Throwable exception = assertThrows(FieldNotValidException.class, () -> validationService.checkExistingEventCause(eventCause));
		assertEquals("Already exists Event and Cause Code combination", exception.getMessage());
		assertEquals("eventCause", ((FieldNotValidException)exception).getInvalidFieldName());
	}
	
	private Row generateRow(String path) throws IOException, InvalidFormatException {
		File file = new File(absolutePath + path);
		
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.rowIterator();
		rowIterator.next();
		workbook.close();
		return rowIterator.next();
	}
	
}
