package com.callfailures.dao;

import static org.junit.Assert.*;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;
import com.callfailures.utils.test.EntityGenerator;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
	   private static final int mCc = 238;
	   private static final int mNc = 1;
	   private static final String country = "Denmark";
	   private static final String operator = "TDC-DK";
	   
	   @Test
	   public void testCreateMarketOperator() {
		   final MarketOperator marketOperator = entityGenerator.parseMobileCountryNetworkCode(mCc, mNc, country, operator);
		   marketOperatorDAO.create(marketOperator);
	   }

	   @Test
	   public void testGetMarketOperatorCountryDescription() {
		   final MarketOperator queryResult = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(mCc, mNc));
		   assertEquals(country, queryResult.getCountryDesc());
	   }
	   
	   @Test
	   public void testGetMarketOperatorDescription() {
		   final MarketOperator queryResult = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(mCc, mNc));
		   assertEquals(operator, queryResult.getOperatorDesc());
	   }
	   
	   @Test
	   public void testGetMarketOperatorCountryCode() {
		   final MarketOperator queryResult = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(mCc, mNc));
		   assertEquals(mCc, queryResult.getMarketOperatorId().getCountryCode());
	   }
	   
	   @Test
	   public void testGetMarketOperatorCode() {
		   final MarketOperator queryResult = marketOperatorDAO.getMarketOperator(new MarketOperatorPK(mCc, mNc));
		   assertEquals(mNc, queryResult.getMarketOperatorId().getOperatorCode());
	   }
	   
	   @Test
	   public void testFailureGetMarketOperator() {
		   assertEquals(null, marketOperatorDAO.getMarketOperator(new MarketOperatorPK(220, 23)));
	   }

	}