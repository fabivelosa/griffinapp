package com.callfailures.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.callfailures.entity.Upload;
import com.callfailures.services.FileService;

@Startup
@Singleton
public class FileListener {

	@Inject
	FileService fileService;

	@PostConstruct
	public void init() {
		System.err.println("File Listener running");
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
					// Change directory to server directory
					final Path directory = Paths.get(System.getProperty("user.dir") + "/fileUploads/");
					final WatchKey watchKey = directory.register(FileSystems.getDefault().newWatchService(),
							StandardWatchEventKinds.ENTRY_CREATE);
					while (true) {
						for (final WatchEvent<?> event : watchKey.pollEvents()) {
							final Path filePath = directory.resolve((Path) event.context());
							final String fileName = filePath.toFile().getName();
							final int dotIndex = fileName.lastIndexOf('.');
							final String fileEnd = fileName.substring(dotIndex);
							if (".xlsx".equals(fileEnd)) {
								System.out.println("Processing: " + filePath.getFileName());
								System.out.println("Path : " + filePath);
								final File file = new File(filePath.toString());
								final Upload currentUpload = fileService.createUploadFile();
								fileService.processFile(new XSSFWorkbook(file), currentUpload);
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