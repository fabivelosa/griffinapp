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
	private long hier_3;
    private long hier_32;
	private long hier_321;
	
	
	public Date getEventTime() {
		return eventTime;
	}
	
	public void setEventTime(final Date eventTime) {
		this.eventTime = eventTime;
	}
	
	public int getEventID() {
		return eventID;
	}
	
	public void setEventID(final int eventID) {
		this.eventID = eventID;
	}
	
	public boolean isFailureClass() {
		return failureClass;
	}
	
	public void setFailureClass(final boolean failureClass) {
		this.failureClass = failureClass;
	}
	
	public int getUeType() {
		return ueType;
	}
	
	public void setUeType(final int ueType) {
		this.ueType = ueType;
	}
	
	public int getMarketCode() {
		return marketCode;
	}
	
	public void setMarketCode(final int marketCode) {
		this.marketCode = marketCode;
	}
	
	public int getOperatorCode() {
		return operatorCode;
	}
	
	public void setOperatorCode(final int operatorCode) {
		this.operatorCode = operatorCode;
	}
	
	public int getCellID() {
		return cellID;
	}
	
	public void setCellID(final int cellID) {
		this.cellID = cellID;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public void setDuration(final int duration) {
		this.duration = duration;
	}
	
	public int getCauseCode() {
		return causeCode;
	}
	
	public void setCauseCode(final int causeCode) {
		this.causeCode = causeCode;
	}
	
	public int getNeVersion() {
		return neVersion;
	}
	
	public void setNeVersion(final int neVersion) {
		this.neVersion = neVersion;
	}
	
	public long getImsiID() {
		return imsiID;
	}
	
	public void setImsiID(final long imsiID) {
		this.imsiID = imsiID;
	}
	
	public long getHier_3() {
		return hier_3;
	}
	
	public void setHier_3(final long hier_3) {
		this.hier_3 = hier_3;
	}
	
	public long getHier_32() {
		return hier_32;
	}
	
	public void setHier_32(final long hier_32) {
		this.hier_32 = hier_32;
	}
	
	public long getHier_321() {
		return hier_321;
	}
	
	public void setHier_321(final long hier_321) {
		this.hier_321 = hier_321;
	}
	
	
}
