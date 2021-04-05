package com.callfailures.services;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.entity.FailureClass;
import com.callfailures.parsingutils.ParsingResponse;

@Local
public interface FailureClassService {

	FailureClass findById(final int failureClass);

	void create(FailureClass obj);

	ParsingResponse<FailureClass> read(final Workbook workbook);
}
