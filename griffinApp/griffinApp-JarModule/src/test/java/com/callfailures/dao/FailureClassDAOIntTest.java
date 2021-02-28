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

@RunWith(Arquillian.class)
public class FailureClassDAOIntTest {
	   @Deployment
	   public static JavaArchive createTestArchive() {
	      return ShrinkWrap.create(JavaArchive.class, "test.jar")
	         .addClasses(FailureClassDAO.class, Events.class, EventCause.class, 
	        		 MarketOperator.class, UserEquipment.class, FailureClass.class,
	        		 EventCausePK.class, MarketOperatorPK.class)
	         .addAsManifestResource(
	            "arquillian-persistence/persistence.xml", 
	            ArchivePaths.create("persistence.xml"));
	   }


	   @Inject
	   private FailureClassDAO failureClassDAO;
	   
	   private final FailureClass failureClass = new FailureClass();

	   @Test
	   public void testCanPersistUserObject() {
		   failureClass.setFailureClass(0);
		   failureClass.setFailureDesc("sample");
		   failureClassDAO.create(failureClass);
		   final FailureClass queryFailureClass = failureClassDAO.getFailureClass(0);
		   assertEquals(0, queryFailureClass.getFailureClass());
	   }

	}