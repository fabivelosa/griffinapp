package com.callfailures.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.callfailures.dto.EventsUploadResponseDTO;
import com.callfailures.entity.EventCause;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.UserEquipment;
import com.callfailures.parsingutils.ParsingResponse;
import com.callfailures.services.EventCauseService;
import com.callfailures.services.EventService;
import com.callfailures.services.FailureClassService;
import com.callfailures.services.MarketOperatorService;
import com.callfailures.services.UserEquipmentService;

@Path("/file")
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

	private final String UPLOADED_FILE_PATH = System.getProperty("user.dir") + "/fileUploads/";

	@POST
	@Path("/upload")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes("multipart/form-data")
	public Response uploadFile(final MultipartFormDataInput input) {

		final long startNano = System.nanoTime();

		String fileName = "";

		final Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		final List<InputPart> inputParts = uploadForm.get("uploadedFile");
		final List<EventsUploadResponseDTO> uploadsOverallResult = new ArrayList<>();
		File sheet = null;

		for (final InputPart inputPart : inputParts) {

			try {

				final MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				// convert the uploaded file to inputstream
				final InputStream inputStream = inputPart.getBody(InputStream.class, null);

				final byte[] bytes = IOUtils.toByteArray(inputStream);

				// constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;

				sheet = writeFile(bytes, fileName);

				System.out.println("Done upload");
				System.out.println("name " + sheet.getName());

				final ParsingResponse<EventCause> eventCauses = causeService.read(sheet);
				final ParsingResponse<FailureClass> failureClasses = failClassService.read(sheet);
				final ParsingResponse<UserEquipment> userEquipment = userEquipmentService.read(sheet);
				final ParsingResponse<MarketOperator> marketOperator = marketOperatorService.read(sheet);
				final ParsingResponse<Events> events = eventService.read(sheet);

				generateResponseEntity(uploadsOverallResult, eventCauses, failureClasses, userEquipment, marketOperator,
						events);

				System.out.println("Done read");

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		final long endNano = System.nanoTime();

		final long duration = (endNano - startNano) / 1000000000;

		System.out.println("It took " + duration + "seconds to validate and store the data");

		return Response.status(200).entity(uploadsOverallResult).build();
	}

	private void generateResponseEntity(final List<EventsUploadResponseDTO> uploadsOverallResult,
			final ParsingResponse<EventCause> eventCauses, final ParsingResponse<FailureClass> failureClasses,
			final ParsingResponse<UserEquipment> userEquipment, final ParsingResponse<MarketOperator> marketOperator,
			final ParsingResponse<Events> events) {
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

	private File writeFile(final byte[] content, final String filename) throws IOException {

		final File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		final FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

		return file;

	}
}