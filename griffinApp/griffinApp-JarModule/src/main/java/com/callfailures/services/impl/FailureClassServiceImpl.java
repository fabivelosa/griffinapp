package com.callfailures.services.impl;

import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.dao.FailureClassDAO;
import com.callfailures.entity.FailureClass;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.FailureClassService;
import com.callfailures.services.ValidationService;

@Stateless
public class FailureClassServiceImpl implements FailureClassService {

	@Inject
	FailureClassDAO failureClassDAO;

	@Inject
	ValidationService validationService;

	@Override
	public FailureClass findById(final int failureclassId) {

		return failureClassDAO.getFailureClass(failureclassId);
	}

	@Override
	public void create(final FailureClass obj) {
		failureClassDAO.create(obj);

	}

	@Override
	public ParsingResponse<FailureClass> read(final Workbook workbook) {
		final ParsingResponse<FailureClass> result = new ParsingResponse<>();

		final Sheet sheet = workbook.getSheetAt(2);
		final Iterator<Row> rowIterator = sheet.rowIterator();
		FailureClass failureClass = null;
		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			final Iterator<Cell> cellIterator = row.cellIterator();

			failureClass = new FailureClass();

			Cell cell = cellIterator.next();

			try {
				failureClass.setFailureClass((int) cell.getNumericCellValue());
				cell = cellIterator.next();
				failureClass.setFailureDesc(cell.getStringCellValue());

				if (validationService.checkExistingFailureClass(failureClass) == null) {
					failureClassDAO.create(failureClass);
					result.addValidObject(failureClass);
				}

			} catch (Exception e) {
				result.addInvalidRow(new InvalidRow(cell.getRowIndex(), e.getMessage()));
			}
		}

		return result;
	}
}