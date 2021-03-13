package com.callfailures.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.callfailures.entity.Events;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneModelSummary;
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
	private final static int eventId = 4098, failureId = 1, ueId = 21060800, mCc = 100, mNc = 930, cellId = 4, duration = 1000,  causeCode = 0 ;
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
	}

	
	@Test
	void testFindCallFailuresCountByIMSIAndDateOneResult() {
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
		
	   List<Events> events = eventDAO.findAllEvents();
	   	   
	   assertEquals(1, events.size());
	   Events retrievedEvent = events.iterator().next();
	   
	   assertEquals(IMSI, retrievedEvent.getImsi());
	   assertEquals(eventTime, retrievedEvent.getDateTime());
	   assertEquals(eventId, retrievedEvent.getEventCause().getEventCauseId().getEventCauseId());
	   assertEquals(causeCode, retrievedEvent.getEventCause().getEventCauseId().getCauseCode());
	   assertEquals(eventCauseDescription, retrievedEvent.getEventCause().getDescription());
	   assertEquals(failureClassDescription, retrievedEvent.getFailureClass().getFailureDesc());
	   assertEquals(failureId, retrievedEvent.getFailureClass().getFailureClass());
	   assertEquals(ueId, retrievedEvent.getUeType().getTac());
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

	   IMSISummary imsiSummary = eventDAO.findCallFailuresCountByIMSIAndDate(IMSI, startTime, endTime);
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
		

	   List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   IMSISummary imsiSummary = eventDAO.findCallFailuresCountByIMSIAndDate(IMSI, startTime.plusMinutes(6), endTime);
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
		

	   List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   PhoneModelSummary phoneModelSummary = eventDAO.findCallFailuresCountByPhoneModelAndDate(phoneModel, startTime, endTime);
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
		

	   List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   PhoneModelSummary phoneModelSummary = eventDAO.findCallFailuresCountByPhoneModelAndDate(phoneModel, startTime.plusMinutes(6), endTime);
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
		

	   List<Events> events = eventDAO.findAllEvents();
	   
	   assertEquals(1, events.size());
	   
	   PhoneModelSummary phoneModelSummary = eventDAO.findCallFailuresCountByPhoneModelAndDate("abc", startTime, endTime);
	   assertNull(phoneModelSummary);
	}
	
}


