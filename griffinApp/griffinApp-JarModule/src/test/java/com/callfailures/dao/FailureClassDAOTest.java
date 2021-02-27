package com.callfailures.dao;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.internal.verification.Times;
import org.mockito.runners.MockitoJUnitRunner;

import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;

@RunWith(MockitoJUnitRunner.class)
public class FailureClassDAOTest {

	private final EntityManager entityManager = mock(EntityManager.class);
	private static final int failureClassID = 1;
	private FailureClass failureClass;

	@InjectMocks
	private FailureClassDAO failureClassDAO;

	@Before
	public void setUp() throws Exception {
		failureClass = new FailureClass();
		failureClass.setFailureClass(0);
		failureClass.setFailureDesc("Sample");
	}

	@Test
	public void testSuccessForGetEvent() {
		when(entityManager.find(FailureClass.class, failureClassID)).thenReturn(failureClass);
		assertEquals(failureClass, failureClassDAO.getFailureClass(failureClassID));
		verify(entityManager, new Times(1)).find(FailureClass.class,failureClassID); 
	}

	@Test
	public void testFailureForGetEvent() {
		when(entityManager.find(FailureClass.class, failureClassID)).thenReturn(null);
		assertEquals(null, failureClassDAO.getFailureClass(failureClassID));
		verify(entityManager, new Times(1)).find(FailureClass.class,failureClassID); 
	}
	
	@Test
	public void testSuccessForCreate() {
		Mockito.doNothing().when(entityManager).persist(failureClass);
	    failureClassDAO.create(failureClass);
	    verify(entityManager, new Times(1)).persist(failureClass); 

	}
}
