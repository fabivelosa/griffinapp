package com.callfailures.utils.test;

import javax.persistence.EntityManager;

import org.junit.Ignore;

@Ignore
public class DBCommandTransactionalExecutor {
	private EntityManager entityManager;

	public DBCommandTransactionalExecutor(final EntityManager em) {
		this.entityManager = em;
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