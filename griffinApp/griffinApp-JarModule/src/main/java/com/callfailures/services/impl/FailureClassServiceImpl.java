package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.EventDAO;
import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.services.EventService;
import com.callfailures.services.FailureClassService;
import com.callfailures.services.ValidationService;

@Stateless
public class  FailureClassServiceImpl implements FailureClassService {

	@Inject
	FailureClassDAO failureClassDAO;

	@Inject
	ValidationService validationService;
	
	@Override
	public FailureClass findById(final int  id) {
		
		return failureClassDAO.getFailureClass(id);
	}

}
