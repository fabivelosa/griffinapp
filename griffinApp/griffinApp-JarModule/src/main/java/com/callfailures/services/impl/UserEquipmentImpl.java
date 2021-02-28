package com.callfailures.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.UserEquipment;
import com.callfailures.services.UserEquipmentService;

public class UserEquipmentImpl implements UserEquipmentService {

	@Inject
	UserEquipmentDAO userEquipmentDAO;

	@Override
	public UserEquipment findById(int id) {
		// TODO Auto-generated method stub
		return userEquipmentDAO.getUserEquipment(id);
	}

	@Override
	public void create(UserEquipment obj) {
		// TODO Auto-generated method stub
		userEquipmentDAO.create(obj);
	}

	@Override
	public ArrayList<UserEquipment> read(final File workbookFile) {
		// TODO Auto-generated method stub
		final ArrayList<UserEquipment> result = new ArrayList<>();

		try {

			final Workbook workbook = new XSSFWorkbook(workbookFile);
			final Sheet sheet = workbook.getSheetAt(3);
			final Iterator<Row> iterator = sheet.iterator();
			System.out.println("\n\nIterating over Rows and Columns using Iterator\n");
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
				userEquipment.setModel(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setVendorName(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setAccessCapability(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setDeviceType(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setUeType(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setDeviceOS(cell.getStringCellValue());
				cell = cellIterator.next();
				userEquipment.setInputMode(cell.getStringCellValue());
				userEquipmentDAO.create(userEquipment);
				result.add(userEquipment);
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
