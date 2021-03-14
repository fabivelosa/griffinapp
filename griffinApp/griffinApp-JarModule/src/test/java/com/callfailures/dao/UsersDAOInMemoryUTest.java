package com.callfailures.dao;



import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import com.callfailures.utils.test.DBCommandTransactionalExecutor;

class UsersDAOInMemoryUTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private UsersDAO usersDAO = new UsersDAO();
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;


	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		usersDAO.entityManager = entityManager;

	}

	
	@Test
	void testAddUsert() {
		
		System.out.println("I am running");
//		dBCommandTransactionalExecutor.executeCommand(() -> {
//
//		   return null;
//		});
		
	   	   

	}
	
	
	
}


