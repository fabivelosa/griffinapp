package com.callfailures.dao;

import static org.junit.Assert.*;
import javax.inject.Inject;
import org.junit.runners.MethodSorters;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.FixMethodOrder;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(Arquillian.class)
public class FailureClassDAOIntegrationTest {
	   @Deployment
	   public static JavaArchive createTestArchive() {
	      return ShrinkWrap.create(JavaArchive.class, "test.jar")
	         .addClasses(FailureClassDAO.class, Events.class, EventCause.class, 
	        		 MarketOperator.class, UserEquipment.class, FailureClass.class,
	        		 EventCausePK.class, MarketOperatorPK.class, EntityGenerator.class)
	         .addAsManifestResource(
	            "arquillian-persistence/persistence.xml", 
	            ArchivePaths.create("persistence.xml"));
	   }


	   @Inject
	   private FailureClassDAO failureClassDAO;
	   
	   private FailureClass failureClass = new FailureClass();
	   private final EntityGenerator entityGenerator = new EntityGenerator();
	   private static final int failureId = 0;
	   private static final String failureClassDescription = "sample";
	   
	   @Test
	   public void testCreate() {
		   failureClass = entityGenerator.parseFailureClass(failureId, failureClassDescription);
		   failureClassDAO.create(failureClass);
	   }

	   @Test
	   public void testGetFailureClassId() {
		   final FailureClass queryResult = failureClassDAO.getFailureClass(0);
		   assertEquals(0, queryResult.getFailureClass());
	   }
	   
	   @Test
	   public void testGetFailureClassDescription() {
		   final FailureClass queryResult = failureClassDAO.getFailureClass(0);
		   assertEquals(failureClassDescription, queryResult.getFailureDesc());
	   }
	      
	   @Test
	   public void testFailureGetFailureClass() {
		   assertEquals(null, failureClassDAO.getFailureClass(1));
	   }
   
	}