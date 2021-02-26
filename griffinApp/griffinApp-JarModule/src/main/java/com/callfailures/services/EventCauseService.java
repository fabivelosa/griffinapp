package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;

@Local
public interface EventCauseService {

	EventCause findById(final EventCausePK id);

	void create(EventCause obj);
}
