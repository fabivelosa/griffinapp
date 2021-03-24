package com.callfailures.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Type;

@Entity(name = "upload")
@XmlRootElement
@Table(name = "upload")
public class Upload {
	@Id
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID uploadID;

	private String uploadStatus;

	public UUID getUploadID() {
		return uploadID;
	}

	public void setUploadID(final UUID uploadID) {
		this.uploadID = uploadID;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(final String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

}