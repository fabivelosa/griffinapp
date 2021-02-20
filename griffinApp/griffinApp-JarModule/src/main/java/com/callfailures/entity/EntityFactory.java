package com.callfailures.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class EntityFactory {

	/**
	 * @param args
	 */
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("event");
	EntityManager em = emf.createEntityManager();
}