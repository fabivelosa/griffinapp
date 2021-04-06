package com.callfailures.services.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.dao.UploadDAO;
import com.callfailures.dto.EventsUploadResponseDTO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.Upload;
import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.InvalidRow;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.EventCauseService;
import com.callfailures.services.EventService;
import com.callfailures.services.FailureClassService;
import com.callfailures.services.FileService;
import com.callfailures.services.MarketOperatorService;
import com.callfailures.services.UserEquipmentService;

@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class FileServiceImpl implements FileService {

	@Inject
	private FailureClassService failClassService;

	@Inject
	private EventCauseService causeService;

	@Inject
	private EventService eventService;

	@Inject
	private UserEquipmentService userEquipmentService;

	@Inject
	private MarketOperatorService marketOperatorService;

	@Inject
	private UploadDAO uploadDAO;

	private static final String DOWNLOADFILEPATH = System.getProperty("jboss.home.dir")
			+ "/welcome-content/fileDownloads/";
	private ParsingResponse<Events> eventsList;
	private Collection<InvalidRow> invalidRows;
	private Collection<Events> validObjects;

	@Override
	public void processFile(final Workbook workbook, final Upload currentUpload) {
		new Thread() {
			public void run() {

				final List<EventsUploadResponseDTO> uploadsOverallResult = new ArrayList<>();
				final long startNano = System.nanoTime();

				final ParsingResponse<EventCause> eventCauses = causeService.read(workbook);
				updateProgress(currentUpload, 5);

				final ParsingResponse<FailureClass> failureClasses = failClassService.read(workbook);
				updateProgress(currentUpload, 10);

				final ParsingResponse<UserEquipment> userEquipment = userEquipmentService.read(workbook);
				updateProgress(currentUpload, 15);

				final ParsingResponse<MarketOperator> marketOperator = marketOperatorService.read(workbook);
				updateProgress(currentUpload, 20);

				final long starEvents = System.nanoTime();
				final ParsingResponse<Events> events = processEvents(currentUpload, workbook);

				generateReportFile(uploadsOverallResult, eventCauses, failureClasses, userEquipment, marketOperator,
						events, currentUpload);

				updateProgress(currentUpload, 100);

				final long endNano = System.nanoTime();
				final long duration = (endNano - startNano) / 1000000000;
				final long durationOthers = (starEvents - startNano) / 1000000000;
				final long durationEvents = (endNano - starEvents) / 1000000000;
				System.out.println("It took " + duration + "seconds to validate and store the data");
				System.out.println("It took " + durationOthers + "seconds to validate and store the others data");
				System.out.println("It took " + durationEvents + "seconds to validate and store the events data");
			}
		}.start();

	}

	private ParsingResponse<Events> processEvents(final Upload currentUpload, final Workbook workbook) {
		final int num_threads = 4;
		eventsList = new ParsingResponse<Events>();
		validObjects = new ArrayList<>();
		invalidRows = new ArrayList<>();

		final Sheet eventSheet = workbook.getSheetAt(0);

		int rowTotal = eventSheet.getLastRowNum();
		if ((rowTotal > 0) || eventSheet.getPhysicalNumberOfRows() > 0) {
			rowTotal++;
		}

		final int slice = rowTotal / num_threads; // 30000/4=7500

		final Thread thread1 = new Thread() {
			public void run() {
				System.out.println("Init :" + Thread.currentThread().getName() + " from id: " + 1 + " to: "
						+ (slice - 1) + " at " + System.currentTimeMillis());
				eventsList = eventService.read(eventSheet, 1, slice - 1, currentUpload);
				invalidRows.addAll(eventsList.getInvalidRows());
				validObjects.addAll(eventsList.getValidObjects());
				System.out.println("End :" + Thread.currentThread().getName() + " at " + System.currentTimeMillis());

			}
		};

		final int slice2 = (slice * 2) - 1;

		final Thread thread2 = new Thread() {
			public void run() {
				System.out.println("Init :" + Thread.currentThread().getName() + " from id: " + slice + " to: " + slice2
						+ " at " + System.currentTimeMillis());
				eventsList = eventService.read(eventSheet, slice, slice2, currentUpload);
				invalidRows.addAll(eventsList.getInvalidRows());
				validObjects.addAll(eventsList.getValidObjects());
				System.out.println("End :" + Thread.currentThread().getName() + " at " + System.currentTimeMillis());

			}
		};

		final int slice3 = (slice * 3) - 1;

		final Thread thread3 = new Thread() {
			public void run() {
				System.out.println("Init :" + Thread.currentThread().getName() + " from id: " + slice * 2 + " to: "
						+ slice3 + " at " + System.currentTimeMillis());
				eventsList = eventService.read(eventSheet, slice * 2, slice3, currentUpload);
				invalidRows.addAll(eventsList.getInvalidRows());
				validObjects.addAll(eventsList.getValidObjects());
				System.out.println("End :" + Thread.currentThread().getName() + " at " + System.currentTimeMillis());

			}
		};

		final Thread thread4 = new Thread() {
			public void run() {
				System.out.println("Init :" + Thread.currentThread().getName() + " from id: " + (slice * 3) + " to: "
						+ eventSheet.getLastRowNum() + " at " + System.currentTimeMillis());
				eventsList = eventService.read(eventSheet, slice * 3, eventSheet.getLastRowNum(), currentUpload);
				invalidRows.addAll(eventsList.getInvalidRows());
				validObjects.addAll(eventsList.getValidObjects());
				System.out.println("End :" + Thread.currentThread().getName() + " at " + System.currentTimeMillis());

			}
		};

		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();

		try {
			thread1.join();
			thread2.join();
			thread3.join();
			thread4.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		updateProgress(currentUpload, 95);
		eventsList.setInvalidRows(invalidRows);
		eventsList.setValidRows(validObjects);
		return eventsList;

	}

	private void generateReportFile(final List<EventsUploadResponseDTO> uploadsOverallResult,
			final ParsingResponse<EventCause> eventCauses, final ParsingResponse<FailureClass> failureClasses,
			final ParsingResponse<UserEquipment> userEquipment, final ParsingResponse<MarketOperator> marketOperator,
			final ParsingResponse<Events> events, final Upload reportFile) {

		uploadsOverallResult.add(new EventsUploadResponseDTO("Event Cause", eventCauses.getValidObjects().size(),
				eventCauses.getInvalidRows()));

		uploadsOverallResult.add(new EventsUploadResponseDTO("Failure Class", failureClasses.getValidObjects().size(),
				failureClasses.getInvalidRows()));

		uploadsOverallResult.add(new EventsUploadResponseDTO("User Equipment", userEquipment.getValidObjects().size(),
				userEquipment.getInvalidRows()));

		uploadsOverallResult.add(new EventsUploadResponseDTO("MCC-NCC", marketOperator.getValidObjects().size(),
				marketOperator.getInvalidRows()));

		uploadsOverallResult.add(
				new EventsUploadResponseDTO("Event Data", events.getValidObjects().size(), events.getInvalidRows()));

		writeFileToServer(uploadsOverallResult, reportFile);

	}

	private void writeFileToServer(final List<EventsUploadResponseDTO> uploadsOverallResult, final Upload reportFile) {
		String table = "";
		final String filename = "Error_" + System.currentTimeMillis() + ".txt";
		FileOutputStream file = null;

		try {

			file = new FileOutputStream(DOWNLOADFILEPATH + filename);
			reportFile.setReportFile(filename);

			for (final EventsUploadResponseDTO result : uploadsOverallResult) {

				table = result.getTabName();
				if (result.getTabName().equals(table.toString())) {
					table = result.getTabName();
					final int ignored = result.getErroneousData().size();
					final int valid = result.getValidRowCount();
					file.write(("Table: " + table + " , has " + ignored + " Ignored Rows")
							.getBytes(Charset.forName("UTF-8")));
					file.write(System.getProperty("line.separator").getBytes(Charset.forName("UTF-8")));
					reportFile.setTotalInvalidRecords(reportFile.getTotalInvalidRecords() + ignored);
					reportFile.setTotalValidRecords(reportFile.getTotalValidRecords() + valid);
					;
				}

				for (final InvalidRow invalidItem : result.getErroneousData()) {
					file.write(("Row :" + invalidItem.getRowNumber() + " , Caused :" + invalidItem.getErrorMessage())
							.getBytes(Charset.forName("UTF-8")));
					file.write(System.getProperty("line.separator").getBytes(Charset.forName("UTF-8")));

				}
			}
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// releases all system resources from the streams
			if (file != null) {
				try {
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public Upload createUploadFile() {
		final UUID uploadUUID = UUID.randomUUID();
		final Upload upload = new Upload();
		upload.setUploadID(uploadUUID);
		upload.setUploadStatus(0);
		uploadDAO.create(upload);
		return uploadDAO.getUploadByRef(uploadUUID);

	}

	public void updateProgress(final Upload currentUpload, final int percent) {
		currentUpload.setUploadStatus(percent);
		uploadDAO.update(currentUpload);

	}

}
