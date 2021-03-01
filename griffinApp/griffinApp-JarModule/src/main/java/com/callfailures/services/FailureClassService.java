package com.callfailures.services;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface FailureClassService {

	FailureClass findById(final int id);

	void create(FailureClass obj);

	ParsingResponse<FailureClass> read(File workbookFile);
}
