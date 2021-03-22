package com.callfailures.dao;

import java.time.LocalDateTime;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.callfailures.entity.Events;
import com.callfailures.entity.views.IMSIEvent;
import com.callfailures.entity.views.IMSISummary;
import com.callfailures.entity.views.PhoneModelSummary;
import com.callfailures.entity.views.PhoneFailures;
import com.callfailures.entity.views.UniqueIMSI;

@Stateless
@LocalBean
public class EventDAO {
	private static final String FIND_ALL_EVENTS = "SELECT e FROM event e",
			FIND_IMSI_BY_DATE="SELECT DISTINCT NEW com.callfailures.entity.views.UniqueIMSI(e.imsi) " 
					+ "FROM event e "
					+ "WHERE (e.dateTime BETWEEN :startTime AND :endTime)",
			FIND_CALL_FAILURES_BY_IMSI = "SELECT NEW com.callfailures.entity.views.IMSIEvent(e.imsi,e.eventCause) "
					+"FROM event e "
					+"WHERE e.imsi = :imsi "
					+"GROUP BY e.imsi, e.eventCause",
			FIND_CALL_FAILURES_BY_IMSI_AND_DATE = "SELECT NEW com.callfailures.entity.views.IMSISummary(e.imsi, COUNT(e), SUM(e.duration)) "
					+ "FROM event e "
					+ "WHERE (e.dateTime BETWEEN :startTime AND :endTime) "
					+ "AND e.imsi = :imsi "
					+ "GROUP BY e.imsi",
		        FIND_CALL_FAILURES_BY_PHONEMODEL_AND_DATE = "SELECT NEW com.callfailures.entity.views.PhoneModelSummary(e.ueType.model, COUNT(e)) "
							+ "FROM event e "
							+ "WHERE (e.dateTime BETWEEN :startTime AND :endTime) "
							+ "AND e.ueType.model = :model "
							+ "GROUP BY e.ueType.model",
			FIND_UNIQUE_EVENT_ID_AND_CAUSE_CODE_COUNT = "SELECT NEW com.callfailures.entity.views.PhoneFailures(e.ueType, e.eventCause, COUNT(e)) "
					+ "FROM event e "
					+ "WHERE e.ueType.tac = :tac "
					+ "GROUP BY e.ueType, e.eventCause";

	
	@PersistenceContext
	EntityManager entityManager;


	/**
	 * Stores the Events object to the database
	 * @param event - the object to be persisted
	 */
	public void create(final Events event) {
		entityManager.persist(event);
	}


	/**
	 * Queries all the Events stored in the database
	 * @return the list of the results of events
	 */
	@SuppressWarnings("unchecked")
	public List<Events> findAllEvents() {
		final Query query = entityManager.createQuery(FIND_ALL_EVENTS, Events.class);
		return query.getResultList();
	}

	
	/**
	 * Queries the Unique EventID, and CauseCode combinations, and their count by phone model
	 * @param tac - the unique identifier of the phone model
	 * @return
	 */
	public List<PhoneFailures> findUniqueEventCauseCountByPhoneModel(final int tac) {
		final Query query = entityManager.createQuery(FIND_UNIQUE_EVENT_ID_AND_CAUSE_CODE_COUNT, PhoneFailures.class);
		query.setParameter("tac", tac);
		return query.getResultList();
	}
	
	
	/**
	 * Queries the IMSISummary object which includes the count of call failures and total duration in a given period
	 * @param imsi
	 * @param startTime (inclusive) - the start of the period
	 * @param endTime (inclusive) - the end of the period
	 * @return the IMSISummary object
	 */
	public IMSISummary findCallFailuresCountByIMSIAndDate(final String imsi, final LocalDateTime startTime, final LocalDateTime endTime) {
		final Query query = entityManager.createQuery(FIND_CALL_FAILURES_BY_IMSI_AND_DATE, IMSISummary.class);
		query.setParameter("imsi", imsi);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		try {
			return (IMSISummary) query.getSingleResult();
		}catch(NoResultException  exception) {
			return null;
		}
	}
	
	/**
	 * Queries the PhoneModelSummary object which includes the count of call failures  in a given period
	 * @param ueType
	 * @param startTime (inclusive) - the start of the period
	 * @param endTime (inclusive) - the end of the period
	 * @return the PhoneModelSummary object
	 */
	public PhoneModelSummary findCallFailuresCountByPhoneModelAndDate(final String model, final LocalDateTime startTime, final LocalDateTime endTime) {
		final Query query = entityManager.createQuery(FIND_CALL_FAILURES_BY_PHONEMODEL_AND_DATE, PhoneModelSummary.class);
		query.setParameter("model", model);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		try {
			return (PhoneModelSummary) query.getSingleResult();
		}catch(NoResultException  exception) {
			return null;
		}
	}
	/**	
	 * Query Database for all events of an inputted IMSI
	 * @param imsi
	 * @return list of failures with Event ID and Cause Code for given IMSI
	 */
	@SuppressWarnings("unchecked")
	public List<IMSIEvent> findEventsByIMSI(final String IMSI){
		final Query query = entityManager.createQuery(FIND_CALL_FAILURES_BY_IMSI,IMSIEvent.class);
		query.setParameter("imsi", IMSI);
		
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Query Database for all IMSI with failures between Start and End time submitted
	 * @param startTime (inclusive) - the start of the period
	 * @param endTime (inclusive) - the end of the period
	 * @return list of IMSI for given time period
	 */
	@SuppressWarnings("unchecked")
	public List<UniqueIMSI> findIMSISBetweenDates(final LocalDateTime startTime, final LocalDateTime endTime){
		final Query query = entityManager.createQuery(FIND_IMSI_BY_DATE,UniqueIMSI.class);
		query.setParameter("startTime", startTime);
		query.setParameter("endTime", endTime);
		try {
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
		
	}

}
