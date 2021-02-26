package com.callfailures.services;

import java.io.File;
import java.util.List;

import javax.ejb.Local;

import com.callfailures.entity.Events;

@Local
public interface EventService {

	Events findById(final int eventId);

	void create(Events obj);

	List<Events> read(File workbookFile);

}
