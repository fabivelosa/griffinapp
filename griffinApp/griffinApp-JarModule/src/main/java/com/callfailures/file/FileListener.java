package com.callfailures.fileListening;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class fileListener {

	private static boolean running = true;
	
	@PostConstruct
	public void init() {
		System.out.println("File Listener running");
		try {
			beginWatch();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private void beginWatch() throws IOException{
		System.out.println("File Listener Beginning");
		new Thread() {
			public void run() {
				try {
					WatchService watchService = FileSystems.getDefault().newWatchService();
					//Change directory to server directory
					Path directory = Paths.get("C:\\Users\\Peter\\Documents\\College work\\AIT\\MASE\\Semester2\\Web Technologies\\Week1\\L1 Server setup\\Lab1-1\\wildfly-8.2.1.Final\\bin\\fileUploads");
					WatchKey watchKey =  directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
					
					while(true) {
						for (WatchEvent<?> event : watchKey.pollEvents()) {
							
							Path file = directory.resolve ((Path) event.context());	
							final String fileName = file.toFile().getName();
							final int dotIndex = fileName.lastIndexOf('.');
							final String fileEnd = fileName.substring(dotIndex);
							if (fileEnd.equals(".xlsx")) {
								System.out.println("Uploading: "+ file.getFileName());
								final File uploadFile = file.toFile();
								//Pass file
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
