package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.Events;

@Local
public interface EventService {

	Events findById(final int id);

	void create(Events obj);

}
