package com.callfailures.dao;


import static org.junit.jupiter.api.Assertions.assertEquals;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.FailureClass;
import com.callfailures.utils.test.DBCommandTransactionalExecutor;
import com.callfailures.utils.test.EntityGenerator;

public class FailureClassDAOInMemoryDBTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private FailureClassDAO failureClassDAO;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;
	private FailureClass failureClass = new FailureClass();
	private final EntityGenerator entityGenerator = new EntityGenerator();
	private static final int failureId = 0;
	private static final String failureClassDescription = "sample";


	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();

		failureClassDAO = new FailureClassDAO();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		failureClassDAO.entityManager = entityManager;

		dBCommandTransactionalExecutor.executeCommand(() -> {
			failureClass = entityGenerator.parseFailureClass(failureId, failureClassDescription);
			failureClassDAO.create(failureClass);
			return null;
		});
	}


	@Test
	public void testGetFailureClassId() {
		final FailureClass queryResult = failureClassDAO.getFailureClass(0);
		assertEquals(0, queryResult.getFailureClass());
		assertEquals(failureClassDescription, queryResult.getFailureDesc());
	}

	@Test
	public void testFailureGetFailureClass() {
		assertEquals(null, failureClassDAO.getFailureClass(1));
	}


}
