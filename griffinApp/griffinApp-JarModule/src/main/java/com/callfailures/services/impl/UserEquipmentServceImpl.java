package com.callfailures.services.impl;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.dao.UserEquipmentDAO;
import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.UserEquipmentService;
import com.callfailures.services.ValidationService;

@Stateless
public class UserEquipmentServceImpl implements UserEquipmentService {

	@Inject
	UserEquipmentDAO userEquipmentDAO;
	@Inject
	ValidationService validationService;

	@Override
	public UserEquipment findById(final int userEquipmentid) {
		return userEquipmentDAO.getUserEquipment(userEquipmentid);
	}

	@Override
	public void create(final UserEquipment obj) {
		userEquipmentDAO.create(obj);
	}

	public List<UserEquipment> findAll() {
		return userEquipmentDAO.findAll().stream().sorted(Comparator.comparing(UserEquipment::getModel))
				.collect(Collectors.toList());
	}

	@Override
	public ParsingResponse<UserEquipment> read(final Workbook workbook) {

		final ParsingResponse<UserEquipment> result = new ParsingResponse<>();

		final DataFormatter dataFormatter = new DataFormatter();
		final Sheet sheet = workbook.getSheetAt(3);
		final Iterator<Row> rowIterator = sheet.rowIterator();
		UserEquipment userEquipment = null;
		Row row = rowIterator.next();
		while (rowIterator.hasNext()) {
			row = rowIterator.next();

			final Iterator<Cell> cellIterator = row.cellIterator();

			userEquipment = new UserEquipment();

			Cell cell = cellIterator.next();

			try {
				cell = readCellValue(dataFormatter, userEquipment, cellIterator, cell);

				if (validationService.checkExistingUserEquipmentType(userEquipment) == null) {
					userEquipmentDAO.create(userEquipment);
					result.addValidObject(userEquipment);
				}
			} catch (Exception e) {
				result.addInvalidRow(new InvalidRow(cell.getRowIndex(), e.getMessage()));
			}
		}

		return result;
	}

	private Cell readCellValue(final DataFormatter dataFormatter, final UserEquipment userEquipment,
			final Iterator<Cell> cellIterator, Cell cell) {
		userEquipment.setTac((int) cell.getNumericCellValue());
		cell = cellIterator.next();
		cell = cellIterator.next();
		cell = cellIterator.next();
		userEquipment.setAccessCapability(cell.getStringCellValue());
		cell = cellIterator.next();
		userEquipment.setModel(dataFormatter.formatCellValue(cell));
		cell = cellIterator.next();
		userEquipment.setVendorName(cell.getStringCellValue());
		cell = cellIterator.next();
		userEquipment.setUeType(cell.getStringCellValue());
		cell = cellIterator.next();
		userEquipment.setDeviceOS(cell.getStringCellValue());
		cell = cellIterator.next();
		userEquipment.setInputMode(cell.getStringCellValue());
		return cell;
	}

}
