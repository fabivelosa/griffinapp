package com.callfailures.services.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;

import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;

@RunWith(MockitoJUnitRunner.class)
public class FailureClassServiceImplTest {


	private final FailureClassDAO failureClassDAO = mock(FailureClassDAO.class);
	private static final int failureClassID = 1;
	private FailureClass failureClass;

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
		FailureClass failureClassObj = failureClassServiceImpl.findById(failureClassID);
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
	public void testSuccessForRead() {
		URL url = this.getClass().getResource("/failureClassService/validData.xlsx");
		File workbookFile = new File(url.getFile());
		Mockito.doNothing().when(failureClassDAO).create(any(FailureClass.class));
		Map<String, List<FailureClass>> failureClassSuccess =  failureClassServiceImpl.read(workbookFile);
		assertEquals(true, failureClassSuccess.containsKey("SUCCESS"));
	}
	
	@Test
	public void testFailureForRead() {
		URL url = this.getClass().getResource("/failureClassService/validData.xlsx");
		File workbookFile = new File(url.getFile());
		Mockito.doThrow(Exception.class).when(failureClassDAO).create(any(FailureClass.class));
		Map<String, List<FailureClass>> failureClassSuccess =  failureClassServiceImpl.read(workbookFile);
		assertEquals(true, failureClassSuccess.containsKey("ERROR"));
	}

}
