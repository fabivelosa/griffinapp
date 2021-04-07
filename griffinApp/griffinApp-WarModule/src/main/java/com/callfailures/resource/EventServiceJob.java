package com.callfailures.resource;

import javax.enterprise.inject.Default;

import org.apache.poi.ss.usermodel.Sheet;

import com.callfailures.entity.Events;
import com.callfailures.entity.Upload;
import com.callfailures.parsingutils.ParsingResponse;

@Default
public class EventServiceJob implements Runnable {

	ParsingResponse<Events> parsingResult = new ParsingResponse<>();

	Sheet sheet;
	int ini;
	int end;
	Upload uploadFile;

	public EventServiceJob(final Sheet sheet, final int ini, final int end, final Upload uploadFile) {
		this.sheet = sheet;
		this.ini = ini;
		this.end = end;
		this.uploadFile = uploadFile;
	}

	public ParsingResponse<Events> getParsingResult() {
		return parsingResult;
	}

	public void setParsingResult(final ParsingResponse<Events> parsingResult) {
		this.parsingResult = parsingResult;
	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(final Sheet sheet) {
		this.sheet = sheet;
	}

	public int getIni() {
		return ini;
	}

	public void setIni(final int ini) {
		this.ini = ini;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(final int end) {
		this.end = end;
	}

	public Upload getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(final Upload uploadFile) {
		this.uploadFile = uploadFile;
	}

	@Override
	public void run() {

		System.out.println("Init :" + Thread.currentThread().getName() + " from id: " + ini + " to: " + end + " at "
				+ System.currentTimeMillis());

		System.out.println("End :" + Thread.currentThread().getName() + " at " + System.currentTimeMillis());

	}

}
