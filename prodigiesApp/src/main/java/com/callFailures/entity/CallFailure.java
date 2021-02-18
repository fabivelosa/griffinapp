package com.callFailures.entity;

import java.util.Date;

public class CallFailure {
     
	private Date eventTime;
	private int eventID;
	private boolean failureClass;
	private int ueType;
	private int marketCode;
	private int operatorCode;
	private int cellID;
	private int duration;
	private int causeCode;
	private int neVersion;
	private long imsiID;
	public Date getEventTime() {
		return eventTime;
	}
	public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	public int getEventID() {
		return eventID;
	}
	public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	public boolean isFailureClass() {
		return failureClass;
	}
	public void setFailureClass(boolean failureClass) {
		this.failureClass = failureClass;
	}
	public int getUeType() {
		return ueType;
	}
	public void setUeType(int ueType) {
		this.ueType = ueType;
	}
	public int getMarketCode() {
		return marketCode;
	}
	public void setMarketCode(int marketCode) {
		this.marketCode = marketCode;
	}
	public int getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(int operatorCode) {
		this.operatorCode = operatorCode;
	}
	public int getCellID() {
		return cellID;
	}
	public void setCellID(int cellID) {
		this.cellID = cellID;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getCauseCode() {
		return causeCode;
	}
	public void setCauseCode(int causeCode) {
		this.causeCode = causeCode;
	}
	public int getNeVersion() {
		return neVersion;
	}
	public void setNeVersion(int neVersion) {
		this.neVersion = neVersion;
	}
	public long getImsiID() {
		return imsiID;
	}
	public void setImsiID(long imsiID) {
		this.imsiID = imsiID;
	}
	
	
	

}
