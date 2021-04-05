package com.callfailures.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.callfailures.dao.UploadDAO;
import com.callfailures.entity.Upload;
import com.callfailures.services.FileService;

@Startup
@Singleton
public class FileListener {

	@Inject
	FileService fileService;

	@Inject
	private UploadDAO uploadDAO;

	@PostConstruct
	public void init() {
		System.out.println("File Listener running");
		try {
			beginWatch();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void beginWatch() throws IOException {
		System.out.println("File Listener Beginning");
		new Thread() {
			public void run() {
				try {
					WatchService watchService = FileSystems.getDefault().newWatchService();
					// Change directory to server directory
					Path directory = Paths.get(System.getProperty("user.dir") + "/fileUploads/");
					WatchKey watchKey = directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

					while (true) {
						for (WatchEvent<?> event : watchKey.pollEvents()) {

							Path filePath = directory.resolve((Path) event.context());
							final String fileName = filePath.toFile().getName();
							final int dotIndex = fileName.lastIndexOf('.');
							final String fileEnd = fileName.substring(dotIndex);
							if (fileEnd.equals(".xlsx")) {
								System.out.println("Processing: " + filePath.getFileName());
								System.out.println("Path : " + filePath);
								File file = new File(filePath.toString());
								Workbook workbook = new XSSFWorkbook(file);

								Upload currentUpload = fileService.createUploadFile();

								fileService.processFile(workbook,currentUpload);
							}
						}
					}
				} catch (Exception e) {
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}
		}.start();
	}

	
}