package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.FailureClass;

@Local
public interface FailureClassService {

	public FailureClass findById(final int id);

}
