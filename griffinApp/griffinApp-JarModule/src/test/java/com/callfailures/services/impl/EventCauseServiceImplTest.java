package com.callfailures.services.impl;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;

import com.callfailures.dao.EventCauseDao;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.ValidationService;

@RunWith(MockitoJUnitRunner.class)
public class EventCauseServiceImplTest {

	private final EventCauseDao eventCauseDAO = mock(EventCauseDao.class);
	private final ValidationService validationService = mock(ValidationService.class);
	private EventCause eventCause;
	private EventCausePK eventCausePK;
	private final String absolutePath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

	@InjectMocks
	private EventCauseServiceImpl eventCauseServiceImpl;

	@Before
	public void setUp() throws Exception {
		eventCausePK = new EventCausePK();
		eventCause = new EventCause();
	}

	@Test
	public void testSuccessForFindById() {
		eventCausePK.setEventCauseId(1000);
		eventCausePK.setCauseCode(200);
		eventCause.setDescription("sample");
		eventCause.setEventCauseId(eventCausePK);
		when(eventCauseDAO.getEventCause(eventCausePK)).thenReturn(eventCause);
		final EventCause eventCauseObj = eventCauseServiceImpl.findById(eventCausePK);
		verify(eventCauseDAO, new Times(1)).getEventCause(eventCausePK);
		assertEquals("sample", eventCauseObj.getDescription());
		assertEquals(eventCausePK, eventCauseObj.getEventCauseId());
	}

	@Test
	public void testFailureForFindById() {
		when(eventCauseDAO.getEventCause(eventCausePK)).thenReturn(eventCause);
		assertNotEquals(eventCause, eventCauseServiceImpl.findById(new EventCausePK(1, 1)));
	}

	@Test
	public void testSuccessForCreate() {
		Mockito.doNothing().when(eventCauseDAO).create(eventCause);
		eventCauseServiceImpl.create(eventCause);
		verify(eventCauseDAO, new Times(1)).create(eventCause);

	}

	@Test
	public void testSuccessForRead() throws InvalidFormatException, IOException {
		final File file = new File(absolutePath + "/eventCauseService/validData.xlsx");
		Workbook workbook = new XSSFWorkbook(file);
		Mockito.doNothing().when(eventCauseDAO).create(any(EventCause.class));
		when(validationService.checkExistingEventCause(any(EventCause.class))).thenReturn(null);
		final ParsingResponse<EventCause> parseResult = eventCauseServiceImpl.read(workbook);
		final Collection<EventCause> validObjects = parseResult.getValidObjects();
		assertEquals(false, validObjects.isEmpty());
	}

	@Test
	public void testFailureForRead() throws InvalidFormatException, IOException {
		final File file = new File(absolutePath + "/eventCauseService/invalidData.xlsx");
		Workbook workbook = new XSSFWorkbook(file);
		Mockito.doThrow(Exception.class).when(eventCauseDAO).create(any(EventCause.class));
		final ParsingResponse<EventCause> parseResult = eventCauseServiceImpl.read(workbook);
		final Collection<InvalidRow> invalidRows = parseResult.getInvalidRows();
		assertEquals(false, invalidRows.isEmpty());
	}

}
