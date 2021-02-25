package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.EventDAO;
import com.callfailures.entity.Events;
import com.callfailures.services.EventService;
import com.callfailures.services.ValidationService;

@Stateless
public class  EventServiceImpl implements EventService {

	@Inject
	EventDAO eventDAO;

	@Inject
	ValidationService validationService;
	
	@Override
	public Events findById(final int  id) {
		
		return eventDAO.getEvent(id);
	}

}
