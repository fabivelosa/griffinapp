package com.callfailures.services.impl;

import java.util.Iterator;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.callfailures.entity.Events;
import com.callfailures.exception.FieldNotValidException;
import com.callfailures.services.ValidationService;

@Stateless
public class ValidationServiceImpl implements ValidationService{

	@Inject
	Validator validator;
	
	@Override
	public void validate(Events events) throws FieldNotValidException{
		validateFields(events);	
		
		validateFields(events.getEventCause());
		
		validateFields(events.getMarketOperator());
	
		validateFields(events.getUeType());
				
		validateFields(events.getFailureClass());
		
	}

	private <T> void validateFields(T t) {
		final Set<ConstraintViolation<T>> errors = validator.validate(t);
		
		final Iterator<ConstraintViolation<T>> errorsIterator = errors.iterator();
		
		if (errorsIterator.hasNext()) {
			final ConstraintViolation<T> violation = errorsIterator.next();
			throw new FieldNotValidException(violation.getPropertyPath().toString(), violation.getMessage());
		}
	}


}
