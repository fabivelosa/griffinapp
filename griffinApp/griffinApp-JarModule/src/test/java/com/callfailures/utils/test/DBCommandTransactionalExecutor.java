package com.callfailures.utils.test;

import javax.persistence.EntityManager;

import org.junit.Ignore;

@Ignore
public class DBCommandTransactionalExecutor {
	private final EntityManager entityManager;

	public DBCommandTransactionalExecutor(final EntityManager entitymanager) {
		this.entityManager = entitymanager;
	}

	public <T> T executeCommand(final DBCommand<T> dbCommand) {
		try {
			entityManager.getTransaction().begin();
			final T toReturn = dbCommand.execute();
			entityManager.getTransaction().commit();
			entityManager.clear();
			return toReturn;
		} catch (final Exception e) {
			e.printStackTrace();
			entityManager.getTransaction().rollback();
			throw new IllegalStateException(e);
		}
	}

}