package com.callfailures.dao;


import static org.junit.jupiter.api.Assertions.assertEquals;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.utils.test.DBCommandTransactionalExecutor;
import com.callfailures.utils.test.EntityGenerator;

public class MarketOperatorInMemoryDBTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private MarketOperatorDAO marketOperatorDAO;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;
	private final EntityGenerator entityGenerator = new EntityGenerator();
	final MarketOperator marketOperator = entityGenerator.parseMobileCountryNetworkCode(mCc, mNc, country, operator);
	private static final int mCc = 238;
	private static final int mNc = 1;
	private static final String country = "Denmark";
	private static final String operator = "TDC-DK";


	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();

		marketOperatorDAO = new MarketOperatorDAO();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		marketOperatorDAO.entityManager = entityManager;

		dBCommandTransactionalExecutor.executeCommand(() -> {
			marketOperatorDAO.create(marketOperator);
			return null;
		});
	}

	   @Test
	   public void testGetMarketOperatorCountryDescription() {
		   final MarketOperator queryResult = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(mCc, mNc));
		   assertEquals(country, queryResult.getCountryDesc());
		   assertEquals(operator, queryResult.getOperatorDesc());
		   assertEquals(mCc, queryResult.getMarketOperatorId().getCountryCode());
		   assertEquals(mNc, queryResult.getMarketOperatorId().getOperatorCode());
	   }
	   
	   
	   @Test
	   public void testFailureGetMarketOperator() {
		   assertEquals(null, marketOperatorDAO.getMarketOperator(new MarketOperatorPK(220, 23)));
	   }
}