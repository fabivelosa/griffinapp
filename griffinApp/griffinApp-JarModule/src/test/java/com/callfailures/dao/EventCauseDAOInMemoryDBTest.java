package com.callfailures.dao;


import static org.junit.jupiter.api.Assertions.assertEquals;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.utils.test.DBCommandTransactionalExecutor;
import com.callfailures.utils.test.EntityGenerator;

public class EventCauseDAOInMemoryDBTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private EventCauseDao eventCauseDAO;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;
	private EventCause eventCause = new EventCause();
	private final EntityGenerator entityGenerator = new EntityGenerator();
	private static final int eventId = 4097;
	private static final int causeCode = 0;
	private static final String eventCauseDescription = "sample";


	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();

		eventCauseDAO = new EventCauseDao();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		eventCauseDAO.entityManager = entityManager;

		dBCommandTransactionalExecutor.executeCommand(() -> {
			eventCause = entityGenerator.parseEventCause(eventId, causeCode, eventCauseDescription);
			eventCauseDAO.create(eventCause);
			return null;
		});
	}


	@Test
	public void testGetEventCauseValid() {
		final EventCause queryResult = eventCauseDAO.getEventCause(new EventCausePK(eventId,causeCode));
		assertEquals(eventId, queryResult.getEventCauseId().getEventCauseId());
		assertEquals(causeCode, queryResult.getEventCauseId().getCauseCode());
		assertEquals(eventCauseDescription, queryResult.getDescription());
	}
	
	@Test
	public void testGetEventCauseInvalid() {
		assertEquals(null, eventCauseDAO.getEventCause(new EventCausePK(0,0)));
	}
}