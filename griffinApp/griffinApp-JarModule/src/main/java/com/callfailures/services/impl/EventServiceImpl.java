package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.EventDAO;
import com.callfailures.entity.CallFailure;
import com.callfailures.services.CallFailureService;

@Stateless
public class  CallFailureServiceImpl implements CallFailureService {

	@Inject
	EventDAO eventDAO;

	@Override
	public CallFailure findById(final int  id) {
		
		return eventDAO.getEvent(id);
	}

}
