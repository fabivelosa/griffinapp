package com.callfailures.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
	@Consumes("multipart/form-data")
	public Response uploadFile(final MultipartFormDataInput input) {

		long startNano = System.nanoTime();

		String fileName = "";

		final Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		final List<InputPart> inputParts = uploadForm.get("uploadedFile");
		File sheet = null;

		for (InputPart inputPart : inputParts) {

			try {

				MultivaluedMap<String, String> header = inputPart.getHeaders();
				fileName = getFileName(header);

				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);

				byte[] bytes = IOUtils.toByteArray(inputStream);

				// constructs upload file path
				fileName = UPLOADED_FILE_PATH + fileName;

				sheet = writeFile(bytes, fileName);

				System.out.println("Done upload");
				System.out.println("name " + sheet.getName());

				System.out.println();

				/*
				 * Commenting out the eventService.read(sheet) for now until all the persist
				 * tabs tasks are done The Events (Base Data) is the last tab to read as it
				 * needs to refer to the other tabs for validation IF YOU WANNA TEST IT OUT, (1)
				 * Uncomment the line for eventService.read(sheet) until the line with for loop
				 * (2) Comment out service.read(sheet) above (3) Load the reference dataset by
				 * run the referencedataset.sql script. You can find this script in JAR module's
				 * src/main/resources folder
				 */

				causeService.read(sheet);
				failClassService.read(sheet);
				userEquipmentService.read(sheet);
				marketOperatorService.read(sheet);
				eventService.read(sheet);

				System.out.println("Done read");

//				ParsingResponse<Events> eventsResults = eventService.read(sheet);
//				System.out.println("Valid Events Row Count is : " + eventsResults.getValidObjects().size());
//				System.out.println("Invalid Events Row Count is : " + eventsResults.getInvalidRows().size());
//				for(final InvalidRow row : eventsResults.getInvalidRows()) System.out.println("Row " + row.getRowNumber() + "\t" + row.getErrorMessage());

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		long endNano = System.nanoTime();

		long duration = (endNano - startNano) / 1000000000;

		System.out.println("It took " + duration + "seconds to validate and store the data");

		return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();

	}

	/**
	 * header sample { Content-Type=[image/png], Content-Disposition=[form-data;
	 * name="file"; filename="filename.extension"] }
	 **/
	// get uploaded filename
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");

		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");

				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	private File writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

		return file;

	}
}