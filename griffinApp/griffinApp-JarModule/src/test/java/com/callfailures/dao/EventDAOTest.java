package com.callfailures.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.callfailures.entity.Events;

@RunWith(MockitoJUnitRunner.class)
public class EventDAOTest {

	private final EntityManager entityManager = mock(EntityManager.class);
	private static final int eventID = 1;
	private Events callFailure;

	@InjectMocks
	private EventDAO eventDAO;

	@Before
	public void setUp() throws Exception {
		callFailure = new Events();
	}

	@Test
	public void testSuccessForGetEvent() {
		when(entityManager.find(Events.class, eventID)).thenReturn(callFailure);
		assertEquals(callFailure, eventDAO.getEvent(eventID));
	}

	@Test
	public void testFailureForGetEvent() {
		when(entityManager.find(Events.class, eventID)).thenReturn(callFailure);
		assertNotEquals(callFailure, eventDAO.getEvent(2));
	}

}
