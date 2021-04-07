package com.callfailures.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.FailureClass;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.ValidationService;

@RunWith(MockitoJUnitRunner.class)
public class FailureClassServiceImplTest {

	private final FailureClassDAO failureClassDAO = mock(FailureClassDAO.class);
	private final ValidationService validationService = mock(ValidationService.class);
	private static final int failureClassID = 1;
	private FailureClass failureClass;
	private final String absolutePath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

	@InjectMocks
	private FailureClassServiceImpl failureClassServiceImpl;

	@Before
	public void setUp() throws Exception {
		failureClass = new FailureClass();
	}

	@Test
	public void testSuccessForFindById() {
		failureClass.setFailureClass(0);
		failureClass.setFailureDesc("Sample");
		when(failureClassDAO.getFailureClass(failureClassID)).thenReturn(failureClass);
		final FailureClass failureClassObj = failureClassServiceImpl.findById(failureClassID);
		verify(failureClassDAO, new Times(1)).getFailureClass(failureClassID);
		assertEquals(0, failureClassObj.getFailureClass());
		assertEquals("Sample", failureClassObj.getFailureDesc());
	}

	@Test
	public void testFailureForFindById() {
		when(failureClassDAO.getFailureClass(failureClassID)).thenReturn(failureClass);
		assertNotEquals(failureClass, failureClassServiceImpl.findById(2));
		verify(failureClassDAO, new Times(1)).getFailureClass(2);

	}

	@Test
	public void testSuccessForCreate() {
		Mockito.doNothing().when(failureClassDAO).create(failureClass);
		failureClassServiceImpl.create(failureClass);
		verify(failureClassDAO, new Times(1)).create(failureClass);

	}

	@Test
	public void testSuccessForRead() throws InvalidFormatException, IOException {
		final File file = new File(absolutePath + "/failureClassService/validData.xlsx");
		Workbook workbook = new XSSFWorkbook(file);
		Mockito.doNothing().when(failureClassDAO).create(any(FailureClass.class));
		when(validationService.checkExistingFailureClass(any(FailureClass.class))).thenReturn(null);
		final ParsingResponse<FailureClass> parseResult = failureClassServiceImpl.read(workbook);
		final Collection<FailureClass> validObjects = parseResult.getValidObjects();
		assertEquals(false, validObjects.isEmpty());
	}

	@Test
	public void testFailureForRead() throws InvalidFormatException, IOException {
		final File file = new File(absolutePath + "/failureClassService/invalidData.xlsx");
		Workbook workbook = new XSSFWorkbook(file);
		Mockito.doThrow(Exception.class).when(failureClassDAO).create(any(FailureClass.class));
		when(validationService.checkExistingFailureClass(any(FailureClass.class))).thenReturn(null);
		final ParsingResponse<FailureClass> parseResult = failureClassServiceImpl.read(workbook);
		final Collection<InvalidRow> invalidRows = parseResult.getInvalidRows();
		assertEquals(false, invalidRows.isEmpty());
	}

}
