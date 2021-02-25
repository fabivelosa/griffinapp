package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;

@Local
public interface EventCauseService {

	public EventCause findById(final EventCausePK id);

	public void create(EventCause obj);
}
