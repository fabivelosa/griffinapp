package com.callfailures.services;

import java.io.File;

import javax.ejb.Local;

import com.callfailures.entity.FailureClass;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface FailureClassService {

	FailureClass findById(final int failureClass);

	void create(FailureClass obj);

	ParsingResponse<FailureClass> read(File workbookFile);
}
