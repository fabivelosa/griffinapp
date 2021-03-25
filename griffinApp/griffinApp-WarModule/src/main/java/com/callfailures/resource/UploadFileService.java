package com.callfailures.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
import com.callfailures.services.MarketOperatorService;
import com.callfailures.services.UserEquipmentService;

@Path("/file")
@Stateless
public class UploadFileService {

	@EJB
	private FailureClassService failClassService;

	@EJB
	private EventCauseService causeService;

	@EJB
	private EventService eventService;

	@EJB
	private UserEquipmentService userEquipmentService;

	@EJB
	private MarketOperatorService marketOperatorService;

	@EJB
	private UploadDAO uploadDAO;

	private final String UPLOADFILEPATH = System.getProperty("user.dir") + "/fileUploads/";

	private static final String DOWNLOADFILEPATH = "/Users/fabi/wildfly-22.0.0.Final/welcome-content/fileDownloads/";

	protected File sheet;

	@POST
	@Path("/upload")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes("multipart/form-data")
	@Context
	public Response uploadFile(final MultipartFormDataInput input) {

		final UUID uploadUUID = UUID.randomUUID();
		final Upload upload = new Upload();
		upload.setUploadID(uploadUUID);
		upload.setUploadStatus(0);
		uploadDAO.create(upload);
		final Upload currentUpload = uploadDAO.getUploadByRef(uploadUUID);
		final Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		final List<InputPart> inputParts = uploadForm.get("uploadedFile");
		for (final InputPart inputPart : inputParts) {
			final MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName = getFileName(header);
			try (InputStream inputStream = inputPart.getBody(InputStream.class, null);) {
				final byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path
				fileName = UPLOADFILEPATH + fileName;
				System.out.println(fileName);
				sheet = writeFile(bytes, fileName);
			} catch (IOException e) {
				return Response.status(400).entity(e.getStackTrace()).build();
			}
		}
		// File sheet = null;
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread() {
			public void run() {
				final List<EventsUploadResponseDTO> uploadsOverallResult = new ArrayList<>();
				final long startNano = System.nanoTime();

				final ParsingResponse<EventCause> eventCauses = causeService.read(sheet);
				currentUpload.setUploadStatus(5);
				uploadDAO.update(currentUpload);
				currentUpload.setUploadStatus(10);
				uploadDAO.update(currentUpload);
				final ParsingResponse<FailureClass> failureClasses = failClassService.read(sheet);
				currentUpload.setUploadStatus(15);
				uploadDAO.update(currentUpload);
				final ParsingResponse<UserEquipment> userEquipment = userEquipmentService.read(sheet);
				currentUpload.setUploadStatus(20);
				uploadDAO.update(currentUpload);
				final ParsingResponse<MarketOperator> marketOperator = marketOperatorService.read(sheet);
				currentUpload.setUploadStatus(25);
				uploadDAO.update(currentUpload);
				final ParsingResponse<Events> events = eventService.read(sheet, currentUpload);
				currentUpload.setUploadStatus(95);
				uploadDAO.update(currentUpload);

				generateResponseEntity(uploadsOverallResult, eventCauses, failureClasses, userEquipment, marketOperator,
						events,currentUpload);

				currentUpload.setUploadStatus(100);
				uploadDAO.update(currentUpload);

				final long endNano = System.nanoTime();
				final long duration = (endNano - startNano) / 1000000000;
				System.out.println("It took " + duration + "seconds to validate and store the data");

			}
		}.start();
		return Response.status(200).entity(currentUpload).build();

	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@Path("upload/{id}")
	public Response findUploadById(@PathParam("id") final String uuid) {
		final UUID uploadRef = UUID.fromString(uuid);
		final Upload requestedUpload = uploadDAO.getUploadByRef(uploadRef);
		return Response.status(200).entity(requestedUpload).build();

	}

	private void generateResponseEntity(final List<EventsUploadResponseDTO> uploadsOverallResult,
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
				new EventsUploadResponseDTO("Base Data", events.getValidObjects().size(), events.getInvalidRows()));
		String table = "";
		try {

			final String filename = "Error" + System.currentTimeMillis() + ".txt";
			final FileOutputStream file = new FileOutputStream(DOWNLOADFILEPATH + filename);
			reportFile.setReportFile(filename);
			for (final EventsUploadResponseDTO result : uploadsOverallResult) {

				if (result.getTabName().equals(table.toString())) {
					file.write(("Table: " + table + " , has Ignored Rows").getBytes());
					table = result.getTabName();
					file.write(System.getProperty("line.separator").getBytes());
				}

				for (final InvalidRow invalidItem : result.getErroneousData()) {
					file.write(("Row :" + invalidItem.getRowNumber() + " , Caused :" + invalidItem.getErrorMessage())
							.getBytes());
					file.write(System.getProperty("line.separator").getBytes());
				}
			}
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename
	private String getFileName(final MultivaluedMap<String, String> header) {

		final String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (final String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				final String[] name = filename.split("=");

				final String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private File writeFile(final byte[] content, final String filename) {
		final File file = new File(filename);

		try (FileOutputStream fop = new FileOutputStream(file)) {
			if (!file.exists()) {
				if (!file.createNewFile()) {
					throw new Exception("File is not created");
				}
			}
			fop.write(content);
			fop.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}