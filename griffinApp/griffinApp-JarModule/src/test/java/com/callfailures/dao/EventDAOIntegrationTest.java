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
	   private final EntityGenerator eventGenerator = new EntityGenerator();
	   
	   @Test
	   public void testCanPersistEventObject () {
		final String localDateTime = "03/18/2020 23:45";
		final int eventId = 4098;
		final int failureId = 1;
		final int ueId = 21060800;
		final int mCc = 100;
		final int mNc = 930;
		final int cellId = 4;
		final int duration = 1000;
		final int causeCode = 0;
		final String neVersion = "11B";
		final String hier3Id = "4.81E+18";
		final String hier32Id = "8.23E+18";
		final String hier321Id = "1.15E+18";
		final String eventCauseDescription = "S1 SIG CONN SETUP-SUCCESS";
		final String failureClassDescription = "HIGH PRIORITY ACCESS";
		final String country = "Antigua and Barbuda";
		final String operator = "AT&T Wireless-Antigua AG";
		final String IMSI = "3.45E+14";
		events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDAO.create(events.getUeType());
		   eventDAO.create(events);
		   final Events queryEvent = eventDAO.getEvent(1);
		   assertEquals(1, queryEvent.getEventId());

	   }

	}