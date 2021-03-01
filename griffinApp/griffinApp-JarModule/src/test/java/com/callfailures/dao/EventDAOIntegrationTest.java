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
public class EventDAOIntegrationTest {
	   
	@Deployment
	   public static JavaArchive createTestArchive() {
	      return ShrinkWrap.create(JavaArchive.class, "test.jar")
	         .addClasses(EventDAO.class, Events.class, EventCause.class, 
	        		 MarketOperator.class, UserEquipment.class, FailureClass.class,
	        		 EventCausePK.class, MarketOperatorPK.class, EntityGenerator.class,
	        		 FailureClassDAO.class, MarketOperatorDAO.class, EventCauseDAO.class,
	        		 UserEquipmentDAO.class)
	         .addAsManifestResource(
	            "arquillian-persistence/persistence.xml", 
	            ArchivePaths.create("persistence.xml"));
	   }


	   @Inject
	   private EventDAO eventDAO;
	   @Inject
	   private FailureClassDAO failureClassDAO;
	   @Inject
	   private MarketOperatorDAO marketOperatorDAO;
	   @Inject
	   private EventCauseDAO eventCauseDAO;
	   @Inject
	   private UserEquipmentDAO userEquipmentDAO;
	   
	   private Events events = new Events();
	   private final static EntityGenerator eventGenerator = new EntityGenerator();
	   private final static String localDateTime = "03/18/2020 23:45";
	   private final static int eventId = 4098, failureId = 1, ueId = 21060800, mCc = 100, mNc = 930, cellId = 4, duration = 1000,  causeCode = 0 ;
	   private final static String neVersion = "11B", hier3Id = "4.81E+18", hier32Id = "8.23E+18", hier321Id = "1.15E+18", IMSI = "3.45E+14" ;
	   private final static String eventCauseDescription = "S1 SIG CONN SETUP-SUCCESS";
	   private final static String failureClassDescription = "HIGH PRIORITY ACCESS";
	   private final static String country = "Antigua and Barbuda";
	   private final static String operator = "AT&T Wireless-Antigua AG";
	   
	   @Test
	   public void testCreateEvent() {
		events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDAO.create(events.getUeType());
		   eventDAO.create(events);
	   }
	   
	   @Test
	   public void testGetEventId() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(1, queryEvent.getEventId());
	   }
	   	   
	   @Test
	   public void testGetEventCellId() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(cellId, queryEvent.getCellId());
	   }
	   
	   @Test
	   public void testGetEventDuration() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(duration, queryEvent.getDuration());
	   }
	   
	   @Test
	   public void testGetEventHier321Id() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(hier321Id, queryEvent.getHier321Id());
	   }
	   
	   @Test
	   public void testGetEventHier32Id() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(hier32Id, queryEvent.getHier32Id());
	   }
	   
	   @Test
	   public void testGetEventHier3Id() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(hier3Id, queryEvent.getHier3Id());
	   }
	   
	   @Test
	   public void testGetEventImsi() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(IMSI, queryEvent.getImsi());
	   }
	   
	   @Test
	   public void testGetEventNeVersion() {
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(neVersion, queryEvent.getNeVersion());
	   }
	   	   
	   @Test
	   public void testFailureGetEvent() {
		   assertEquals(null, eventDAO.getEvent(2));
	   }

	}