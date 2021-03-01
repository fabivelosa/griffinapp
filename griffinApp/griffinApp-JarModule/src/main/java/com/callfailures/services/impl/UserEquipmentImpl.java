package com.callfailures.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.UserEquipmentService;

@Stateless
public class UserEquipmentImpl implements UserEquipmentService {

	@Inject
	UserEquipmentDAO userEquipmentDAO;

	ValidationServiceImpl validationService;

	@Override
	public UserEquipment findById(int id) {
		return userEquipmentDAO.getUserEquipment(id);
	}

	@Override
	public void create(UserEquipment obj) {
		userEquipmentDAO.create(obj);
	}

	@Override
	public ParsingResponse<UserEquipment> read(final File workbookFile) {

		final ParsingResponse<UserEquipment> result = new ParsingResponse<>(); 

		try {

			final Workbook workbook = new XSSFWorkbook(workbookFile);
			final DataFormatter df = new DataFormatter();
			final Sheet sheet = workbook.getSheetAt(3);
			final Iterator<Row> iterator = sheet.iterator();
			final Iterator<Row> rowIterator = sheet.rowIterator();
			UserEquipment userEquipment = null;
			Row row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();

				final Iterator<Cell> cellIterator = row.cellIterator();

				userEquipment = new UserEquipment();

				Cell cell = cellIterator.next();
				userEquipment.setTac((int) cell.getNumericCellValue());
				cell = cellIterator.next();
				cell = cellIterator.next();
				cell = cellIterator.next();
				userEquipment.setAccessCapability(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setModel(df.formatCellValue(cell));
				cell = cellIterator.next();
				userEquipment.setVendorName(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setUeType(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setDeviceOS(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setInputMode(cell.getStringCellValue());
				try {
				//	if (validationService.checkExistingUserEquipmentType(userEquipment) == null) {
						userEquipmentDAO.create(userEquipment);
						result.addValidObject(userEquipment);
				//	}
				} catch (Exception e) {
					result.addInvalidRow(new InvalidRow(cell.getRowIndex(), e.getMessage()));
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}

		return result;
	}

}
