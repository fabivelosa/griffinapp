package com.callfailures.entity;

import java.util.UUID;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

@Entity(name = "upload")
@XmlRootElement
@Table(name = "upload")
@Cacheable
public class Upload {
	@Id
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID uploadID;

	private int uploadStatus;
	private String reportFile;
	private int totalValidRecords;
	private int totalInvalidRecords;

	public String getReportFile() {
		return reportFile;
	}

	public void setReportFile(final String reportFile) {
		this.reportFile = reportFile;
	}

	public UUID getUploadID() {
		return uploadID;
	}

	public void setUploadID(final UUID uploadID) {
		this.uploadID = uploadID;
	}

	public int getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(final int uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public int getTotalValidRecords() {
		return totalValidRecords;
	}

	public void setTotalValidRecords(final int totalValidRecords) {
		this.totalValidRecords = totalValidRecords;
	}

	public int getTotalInvalidRecords() {
		return totalInvalidRecords;
	}

	public void setTotalInvalidRecords(final int totalInvalidRecords) {
		this.totalInvalidRecords = totalInvalidRecords;
	}

}