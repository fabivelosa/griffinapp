package com.callfailures.dao;

import static org.junit.Assert.*;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.utils.test.EntityGenerator;

@RunWith(Arquillian.class)
public class MarketOperatorDAOIntegrationTest {
	   @Deployment
	   public static JavaArchive createTestArchive() {
	      return ShrinkWrap.create(JavaArchive.class, "test.jar")
	         .addClasses(MarketOperatorDAO.class, Events.class, EventCause.class, 
	        		 MarketOperator.class, UserEquipment.class, FailureClass.class,
	        		 EventCausePK.class, MarketOperatorPK.class, EntityGenerator.class)
	         .addAsManifestResource(
	            "arquillian-persistence/persistence.xml", 
	            ArchivePaths.create("persistence.xml"));
	   }


	   @Inject
	   private MarketOperatorDAO marketOperatorDAO;
	   
	   private final EntityGenerator entityGenerator = new EntityGenerator();

	   @Test
	   public void testCanPersistMarketOperatorObject() {
		   final int mCc = 238;
		   final int mNc = 1;
		   final String country = "Denmark";
		   final String operator = "TDC-DK";
		   final MarketOperator marketOperator = entityGenerator.parseMobileCountryNetworkCode(mCc, mNc, country, operator);
		   marketOperatorDAO.create(marketOperator);
		   final MarketOperator queryResult = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(mCc, mNc));
		   assertEquals(country, queryResult.getCountryDesc());
	   }

	}