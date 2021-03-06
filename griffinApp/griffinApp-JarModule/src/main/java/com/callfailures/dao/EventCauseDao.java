package com.callfailures.dao;

import com.callfailures.entity.EventCause;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Stateless
@LocalBean
public class EventCauseDao {

  @PersistenceContext
  EntityManager entityManager;

  public EventCause getEventCause(final Object eventCauseId) {
    return entityManager.find(EventCause.class, eventCauseId);
  }

  public void create(final EventCause obj) {
    entityManager.persist(obj);
  }
}
