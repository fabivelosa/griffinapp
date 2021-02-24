package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.exception.FieldNotValidException;

@Local
public interface ValidationService {
	
	/**
	 * Checks the validity of call failure fields
	 * @param callFailure instance that will be validated
	 * @throws FieldNotValidException
	 */
	void validate(Events event) throws FieldNotValidException;
	
}
