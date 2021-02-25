package com.callfailures.services.impl;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.FailureClass;
import com.callfailures.services.FailureClassService;
import com.callfailures.services.ValidationService;

@Stateless
public class FailureClassServiceImpl implements FailureClassService {

	@Inject
	FailureClassDAO failureClassDAO;

	@Inject
	ValidationService validationService;

	@Override
	public FailureClass findById(final int id) {

		return failureClassDAO.getFailureClass(id);
	}

	@Override
	public void create(FailureClass obj) {
		failureClassDAO.create(obj);

	}

}
