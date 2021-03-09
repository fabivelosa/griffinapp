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

@Stateless
@LocalBean
public class EventDAO {
	private static final String FIND_ALL_EVENTS = "SELECT e FROM event e",
			FIND_CALL_FAILURES_BY_IMSI = "SELECT NEW e FROM event e",
			FIND_CALL_FAILURES_BY_IMSI_AND_DATE = "SELECT NEW com.callfailures.entity.views.IMSISummary(e.imsi, COUNT(e), SUM(e.duration)) "
					+ "FROM event e "
					+ "WHERE (e.dateTime BETWEEN :startTime AND :endTime) "
					+ "AND e.imsi = :imsi "
					+ "GROUP BY e.imsi";

	
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
	 * Query Database for all events of an inputted IMSI
	 * @param imsi
	 * @return list of failures with Event ID and Cause Code for given IMSI
	 */
	public List<IMSIEvent> findEventsByIMSI(final String IMSI){
		//final Query query = entityManager.createQuery(qlString);
		return null;
	}
	

}
