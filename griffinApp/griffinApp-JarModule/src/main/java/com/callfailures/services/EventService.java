package com.callfailures.services;

import javax.ejb.Local;

import com.callfailures.entity.Events;

@Local
public interface EventService {

	public Events findById(final int id);

	public void create(Events obj);

}
