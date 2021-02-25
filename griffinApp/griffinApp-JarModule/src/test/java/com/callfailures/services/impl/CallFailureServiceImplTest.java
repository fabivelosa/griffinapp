package com.callfailures.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.callfailures.dao.EventDAO;
import com.callfailures.entity.Events;

@RunWith(MockitoJUnitRunner.class)
public class CallFailureServiceImplTest {

	private final EventDAO eventDAO = mock(EventDAO.class);
	private static final int eventID = 1;
	private Events callFailure;

	@InjectMocks
	private EventServiceImpl callFailureService;

	@Before
	public void setUp() throws Exception {
		callFailure = new Events();
	}

	@Test
	public void testSuccessForFindById() {
		when(eventDAO.getEvent(eventID)).thenReturn(callFailure);
		assertEquals(callFailure, callFailureService.findById(eventID));
	}

	@Test
	public void testFailureForFindById() {
		when(eventDAO.getEvent(eventID)).thenReturn(callFailure);
		assertNotEquals(callFailure, callFailureService.findById(2));

	}
}
