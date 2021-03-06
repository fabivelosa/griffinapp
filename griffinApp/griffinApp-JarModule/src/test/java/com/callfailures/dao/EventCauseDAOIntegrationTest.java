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
public class EventCauseDAOIntegrationTest {
	   @Deployment
	   public static JavaArchive createTestArchive() {
	      return ShrinkWrap.create(JavaArchive.class, "test.jar")
	         .addClasses(EventCauseDao.class, Events.class, EventCause.class, 
	        		 MarketOperator.class, UserEquipment.class, EventCause.class, FailureClass.class,
	        		 EventCausePK.class, MarketOperatorPK.class, EntityGenerator.class)
	         .addAsManifestResource(
	            "arquillian-persistence/persistence.xml", 
	            ArchivePaths.create("persistence.xml"));
	   }


	   @Inject
	   private EventCauseDao eventCauseDAO;
	   
	   private EventCause eventCause = new EventCause();
	   private final EntityGenerator entityGenerator = new EntityGenerator();
	   private static final int eventId = 4097;
	   private static final int causeCode = 0;
	   private static final String eventCauseDescription = "sample";
	   
	   @Test
	   public void testCreate() {
		   eventCause = entityGenerator.parseEventCause(eventId, causeCode, eventCauseDescription);
		   eventCauseDAO.create(eventCause);
	   }

	   @Test
	   public void testGetEventCauseId() {
		   final EventCause queryResult = eventCauseDAO.getEventCause(new EventCausePK(eventId,causeCode));
		   assertEquals(eventId, queryResult.getEventCauseId().getEventCauseId());
	   }
	   
	   @Test
	   public void testGetEventCauseCode() {
		   final EventCause queryResult = eventCauseDAO.getEventCause(new EventCausePK(eventId,causeCode));
		   assertEquals(causeCode, queryResult.getEventCauseId().getCauseCode());
	   }
	   
	   @Test
	   public void testGetEventCauseDescription() {
		   final EventCause queryResult = eventCauseDAO.getEventCause(new EventCausePK(eventId,causeCode));
		   assertEquals(eventCauseDescription, queryResult.getDescription());
	   }
	      
	   @Test
	   public void testFailureGetEventCause() {
		   assertEquals(null, eventCauseDAO.getEventCause(new EventCausePK(0, 0)));
	   }
   
	}