package com.callfailures.dao;

import static org.mockito.Mockito.*;
import javax.persistence.EntityManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import com.callfailures.entity.CallFailure;

@RunWith(MockitoJUnitRunner.class)
public class EventDAOTest {

	private final EntityManager entityManager = mock(EntityManager.class);
	private static final int eventID = 1;
	private CallFailure callFailure;
	
	@InjectMocks
	private  EventDAO eventDAO;

	@Before
	public void setUp() throws Exception {
		callFailure = new CallFailure();
	}

	@Test
	public void testSuccessForGetEvent() {
		when(entityManager.find(CallFailure.class, eventID)).thenReturn(callFailure);
		assertEquals(callFailure, eventDAO.getEvent(eventID));
	}
	
	@Test
	public void testFailureForGetEvent() {
		when(entityManager.find(CallFailure.class, eventID)).thenReturn(callFailure);
		assertNotEquals(callFailure, eventDAO.getEvent(2));
	}
	
}
