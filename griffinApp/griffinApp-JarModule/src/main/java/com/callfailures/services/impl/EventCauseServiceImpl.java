package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.EventCauseDAO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.services.EventCauseService;
import com.callfailures.services.ValidationService;

@Stateless
public class EventCauseServiceImpl implements EventCauseService {

	@Inject
	EventCauseDAO eventCauseDAO;

	@Inject
	ValidationService validationService;

	@Override
	public EventCause findById(EventCausePK id) {
		return eventCauseDAO.getEventCause(id);
	}

}
