package com.callfailures.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
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

@RunWith(MockitoJUnitRunner.class)
public class EventDAOTest {

	private final EntityManager entityManager = mock(EntityManager.class);
	private static final int eventID = 1;
	private Events event;

	@InjectMocks
	private EventDAO eventDAO;

	@Before
	public void setUp() throws Exception {
		event = new Events();
	}

	@Test
	public void testSuccessForGetEvent() {
		when(entityManager.find(Events.class, eventID)).thenReturn(event);
		assertEquals(event, eventDAO.getEvent(eventID));
		verify(entityManager, new Times(1)).find(Events.class,eventID); 
	}

	@Test
	public void testFailureForGetEvent() {
		when(entityManager.find(Events.class, eventID)).thenReturn(null);
		assertEquals(null, eventDAO.getEvent(eventID));
		verify(entityManager, new Times(1)).find(Events.class,eventID); 
	}

	@Test
	public void testSuccessForCreate() {
		Mockito.doNothing().when(entityManager).persist(event);
	    eventDAO.create(event);
	    verify(entityManager, new Times(1)).persist(event); 

	}
	
}
