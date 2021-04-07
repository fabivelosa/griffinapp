package com.callfailures.resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import com.callfailures.dao.UploadDAO;
import com.callfailures.entity.Secured;
import com.callfailures.entity.Upload;
import com.callfailures.services.FileService;

@Path("/file")
@Stateless
@Secured
public class UploadFileService {

	@EJB
	private UploadDAO uploadDAO;

	private final String uploadFilePath = System.getProperty("user.dir") + "/fileUploadsUI/";

	protected File sheet;

	@EJB
	protected FileService fileService;

	@POST
	@Path("/upload")
	@Produces({ MediaType.APPLICATION_JSON })
	@Consumes("multipart/form-data")
	@Context
	@Secured
	public Response uploadFile(final MultipartFormDataInput input) throws InvalidFormatException, IOException {

		final Upload currentUpload = fileService.createUploadFile();

		final Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		final List<InputPart> inputParts = uploadForm.get("uploadedFile");
		for (final InputPart inputPart : inputParts) {
			final MultivaluedMap<String, String> header = inputPart.getHeaders();
			String fileName = getFileName(header);
			try (InputStream inputStream = inputPart.getBody(InputStream.class, null);) {
				final byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path
				fileName = uploadFilePath + fileName;
				System.out.println(fileName);
				sheet = writeFile(bytes, fileName);
			} catch (IOException e) {
				return Response.status(400).entity(e.getStackTrace()).build();
			}
		}

		fileService.processFile(new XSSFWorkbook(sheet), currentUpload);
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
					throw new IOException("File is not created");
				}
			}
			fop.write(content);
			fop.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file;

	}
}