package com.callfailures.services;

import javax.ejb.Local;

import org.apache.poi.ss.usermodel.Workbook;

import com.callfailures.entity.Upload;

@Local
public interface FileService {

	void processFile(Workbook file, Upload upload);

	Upload createUploadFile();

	void updateProgress(final Upload currentUpload, final int percent);
}
