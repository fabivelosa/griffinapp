package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.CallFailure;

@Local
public interface CallFailureService {

	
	public CallFailure findById(final int id);

}
