package com.callfailures.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;
import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.ValidationService;

@RunWith(MockitoJUnitRunner.class)
public class UserEquipmentImplTest {

	private final UserEquipmentDAO userEquipmentDAO = mock(UserEquipmentDAO.class);
	private final ValidationService validationService = mock(ValidationService.class);
	private static final int userEquipmentID = 0;
	private UserEquipment userEquipment;
	private final String absolutePath = Paths.get("src", "test", "resources").toFile().getAbsolutePath();

	@InjectMocks
	private UserEquipmentServceImpl userEquipmentImpl;

	@Before
	public void setUp() throws Exception {
		userEquipment = new UserEquipment();
	}
	
	@Test
	public void testFindAll() {
		userEquipment.setTac(0);
		userEquipment.setModel("Sample");
		List<UserEquipment> equipmentList = new ArrayList<>();
		equipmentList.add(userEquipment);
		when(userEquipmentDAO.findAll()).thenReturn(equipmentList);
		List<UserEquipment> retrievedEquipmentList = userEquipmentImpl.findAll();
		assertEquals(1, retrievedEquipmentList.size());
		verify(userEquipmentDAO, times(1)).findAll();
	}
	
	@Test
	public void testSuccessForFindById() {
		userEquipment.setTac(0);
		userEquipment.setModel("Sample");
		when(userEquipmentDAO.getUserEquipment(userEquipmentID)).thenReturn(userEquipment);
		final UserEquipment userEquipmentObj = userEquipmentImpl.findById(userEquipmentID);
		verify(userEquipmentDAO, new Times(1)).getUserEquipment(userEquipmentID);
		assertEquals(0, userEquipmentObj.getTac());
		assertEquals("Sample", userEquipmentObj.getModel());
	}

	@Test
	public void testFailureForFindById() {
		when(userEquipmentDAO.getUserEquipment(userEquipmentID)).thenReturn(userEquipment);
		assertNotEquals(userEquipment, userEquipmentImpl.findById(2));
		verify(userEquipmentDAO, new Times(1)).getUserEquipment(2);

	}

	@Test
	public void testSuccessForCreate() {
		Mockito.doNothing().when(userEquipmentDAO).create(userEquipment);
		userEquipmentImpl.create(userEquipment);
		verify(userEquipmentDAO, new Times(1)).create(userEquipment);

	}

	@Test
	public void testSuccessForRead() {
		final File workbookFile = new File(absolutePath + "/userEquipmentService/validData.xlsx");
		Mockito.doNothing().when(userEquipmentDAO).create(any(UserEquipment.class));
		when(validationService.checkExistingUserEquipmentType(any(UserEquipment.class))).thenReturn(null);
		final ParsingResponse<UserEquipment> parseResult = userEquipmentImpl.read(workbookFile);
		final Collection<UserEquipment> validObjects = parseResult.getValidObjects();
		assertEquals(false, validObjects.isEmpty());
	}

	@Test
	public void testFailureForRead() {
		final File workbookFile = new File(absolutePath + "/userEquipmentService/invalidData.xlsx");
		Mockito.doThrow(Exception.class).when(userEquipmentDAO).create(any(UserEquipment.class));
		when(validationService.checkExistingUserEquipmentType(any(UserEquipment.class))).thenReturn(null);
		final ParsingResponse<UserEquipment> parseResult = userEquipmentImpl.read(workbookFile);
		final Collection<InvalidRow> invalidRows = parseResult.getInvalidRows();
		assertEquals(false, invalidRows.isEmpty());
	}
}
