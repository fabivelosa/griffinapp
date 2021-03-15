package com.callfailures.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.Events;
import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.utils.test.DBCommandTransactionalExecutor;
import com.callfailures.utils.test.EntityGenerator;

class EventDAOInMemoryUTest {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private EventDAO eventDAO;
	private FailureClassDAO failureClassDAO;
	private MarketOperatorDAO marketOperatorDAO;
	private EventCauseDao eventCauseDAO;
	private UserEquipmentDAO userEquipmentDaO;
	private DBCommandTransactionalExecutor dBCommandTransactionalExecutor;
	private final static EntityGenerator eventGenerator = new EntityGenerator();
	private final static String localDateTime = "03/18/2020 23:45";
	private final static LocalDateTime eventTime = LocalDateTime.of(2020,3,18,23,45);
	private final static LocalDateTime startTime = LocalDateTime.of(2020,3,18,23,40);
	private final static LocalDateTime endTime = LocalDateTime.of(2020,3,18,23,50);
	private final static int eventId = 4098, failureId = 1, tac = 21060800, mCc = 100, mNc = 930, cellId = 4, duration = 1000,  causeCode = 0 ;
	private final static String neVersion = "11B", hier3Id = "4809532081614990000", hier32Id = "8226896360947470000", hier321Id = "1150444940909480000", IMSI = "344930000000011" ;
	private final static String phoneModel = "G410";
	private final static String eventCauseDescription = "S1 SIG CONN SETUP-SUCCESS";
	private final static String failureClassDescription = "HIGH PRIORITY ACCESS";
	private final static String country = "Antigua and Barbuda";
	private final static String operator = "AT&T Wireless-Antigua AG";
	private Events events = new Events();


	@BeforeEach
	public void initTestCase() {
		emf = Persistence.createEntityManagerFactory("eventsInMemoryPU");
		entityManager = emf.createEntityManager();
		eventDAO = new EventDAO();
		failureClassDAO = new FailureClassDAO();
		marketOperatorDAO = new MarketOperatorDAO();
		eventCauseDAO = new EventCauseDao();
		userEquipmentDaO = new UserEquipmentDAO();
		dBCommandTransactionalExecutor = new DBCommandTransactionalExecutor(entityManager);
		eventDAO.entityManager = entityManager;
		failureClassDAO.entityManager = entityManager;
		marketOperatorDAO.entityManager = entityManager;
		eventCauseDAO.entityManager = entityManager;
		userEquipmentDaO.entityManager = entityManager;
		
		dBCommandTransactionalExecutor.executeCommand(() -> {
			events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, tac, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDaO.create(events.getUeType());
		   eventDAO.create(events);
		   return null;
		});
		
	   final List<Events> events = eventDAO.findAllEvents();
	}

	
	@Test
	void testFindCallFailuresCountByIMSIAndDateOneResult() {
	   List<Events> events = eventDAO.findAllEvents();
	   	   
	   assertEquals(1, events.size());
	   final Events retrievedEvent = events.iterator().next();
	   
	   assertEquals(IMSI, retrievedEvent.getImsi());
	   assertEquals(eventTime, retrievedEvent.getDateTime());
	   assertEquals(eventId, retrievedEvent.getEventCause().getEventCauseId().getEventCauseId());
	   assertEquals(causeCode, retrievedEvent.getEventCause().getEventCauseId().getCauseCode());
	   assertEquals(eventCauseDescription, retrievedEvent.getEventCause().getDescription());
	   assertEquals(failureClassDescription, retrievedEvent.getFailureClass().getFailureDesc());
	   assertEquals(failureId, retrievedEvent.getFailureClass().getFailureClass());
	   assertEquals(tac, retrievedEvent.getUeType().getTac());
	   assertEquals(mCc, retrievedEvent.getMarketOperator().getMarketOperatorId().getCountryCode());
	   assertEquals(country, retrievedEvent.getMarketOperator().getCountryDesc());
	   assertEquals(mNc, retrievedEvent.getMarketOperator().getMarketOperatorId().getOperatorCode());
	   assertEquals(operator, retrievedEvent.getMarketOperator().getOperatorDesc());
	   assertEquals(cellId, retrievedEvent.getCellId());
	   assertEquals(duration, retrievedEvent.getDuration());
	   assertEquals(neVersion, retrievedEvent.getNeVersion());
	   assertEquals(hier3Id, retrievedEvent.getHier3Id());
	   assertEquals(hier32Id, retrievedEvent.getHier32Id());
	   assertEquals(hier321Id, retrievedEvent.getHier321Id());

	   final IMSISummary imsiSummary = eventDAO.findCallFailuresCountByIMSIAndDate(IMSI, startTime, endTime);
	   assertEquals(IMSI, imsiSummary.getImsi());
	   assertEquals(1, imsiSummary.getCallFailuresCount());
	   assertEquals(1000, imsiSummary.getTotalDurationMs());
	   assertNotNull(imsiSummary);
	}
	
	
	@Test
	void testFindCallFailuresCountByIMSIAndDateEmptyResult() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDaO.create(events.getUeType());
		   eventDAO.create(events);
		   return null;
		});
		

	   final List<Events> events = eventDAO.findAllEvents();
	   List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   final IMSISummary imsiSummary = eventDAO.findCallFailuresCountByIMSIAndDate(IMSI, startTime.plusMinutes(6), endTime);
	   assertNull(imsiSummary);
	}
	
	@Test
	void testSuccessFindCallFailuresCountByPhoneModelAndDate() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDaO.create(events.getUeType());
		   eventDAO.create(events);
		   return null;
		});
		

	   final List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   final PhoneModelSummary phoneModelSummary = eventDAO.findCallFailuresCountByPhoneModelAndDate(phoneModel, startTime, endTime);
	   assertEquals(phoneModel, phoneModelSummary.getModel());
	   assertEquals(1, phoneModelSummary.getCallFailuresCount());
	   assertNotNull(phoneModelSummary);
	}
	
	@Test
	void testFailureFindCallFailuresCountByPhoneModelAndDate() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDaO.create(events.getUeType());
		   eventDAO.create(events);
		   return null;
		});
		

	   final List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   final PhoneModelSummary phoneModelSummary = eventDAO.findCallFailuresCountByPhoneModelAndDate(phoneModel, startTime.plusMinutes(6), endTime);
	   assertNull(phoneModelSummary);
	}
	
	@Test
	void testFailureFindCallFailuresCountByInvalidPhoneModel() {
		dBCommandTransactionalExecutor.executeCommand(() -> {
			events = eventGenerator.generateCallFailureInstance(localDateTime, eventId, failureId, ueId, mCc, mNc,
					cellId, duration, causeCode, neVersion, IMSI , hier3Id, hier32Id, hier321Id, eventCauseDescription,
					failureClassDescription, country, operator);
		   failureClassDAO.create(events.getFailureClass());
		   marketOperatorDAO.create(events.getMarketOperator());
		   eventCauseDAO.create(events.getEventCause());
		   userEquipmentDaO.create(events.getUeType());
		   eventDAO.create(events);
		   return null;
		});
		

	   final List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   final PhoneModelSummary phoneModelSummary = eventDAO.findCallFailuresCountByPhoneModelAndDate("abc", startTime, endTime);
	   assertNull(phoneModelSummary);
	}
	
	
	@Test
	void testfindEventsByIMSI() {
		
		final String validIMSI = "344930000000011";
		List<IMSIEvent> IMSIevents = eventDAO.findEventsByIMSI(validIMSI);
		assertNotNull(IMSIevents);
		assertEquals(1,IMSIevents.size());
		
		IMSIEvent IMSIEvent = IMSIevents.get(0);
		assertEquals(validIMSI,IMSIEvent.getImsi());
		
		assertNotNull(IMSIEvent.getEventCause());
		
		assertEquals(eventId, IMSIEvent.getEventCause().getEventCauseId().getEventCauseId());
		assertEquals(causeCode, IMSIEvent.getEventCause().getEventCauseId().getCauseCode());
		
	}
	
	@Test
	void testFindUniqueEventCauseCountByPhoneModel() {
		List<Events> events = eventDAO.findAllEvents();
		
		assertEquals(1,events.size());
		
		List<PhoneFailures> phoneFailures = eventDAO.findUniqueEventCauseCountByPhoneModel(tac);
		
		assertEquals(1,phoneFailures.size());
		
		PhoneFailures retrievedPhoneFailure = phoneFailures.get(0);
		
		assertEquals(tac,retrievedPhoneFailure.getUserEquipment().getTac());
		assertEquals(eventId,retrievedPhoneFailure.getEventCause().getEventCauseId().getEventCauseId());
		assertEquals(causeCode,retrievedPhoneFailure.getEventCause().getEventCauseId().getCauseCode());
		assertEquals(1L,retrievedPhoneFailure.getCount());
		
	}
}


