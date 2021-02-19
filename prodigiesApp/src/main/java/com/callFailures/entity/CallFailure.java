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
	final public void setEventTime(Date eventTime) {
		this.eventTime = eventTime;
	}
	
	public int getEventID() {
		return eventID;
	}
	final public void setEventID(int eventID) {
		this.eventID = eventID;
	}
	
	public boolean isFailureClass() {
		return failureClass;
	}
	final public void setFailureClass(boolean failureClass) {
		this.failureClass = failureClass;
	}
	
	public int getUeType() {
		return ueType;
	}
	final public void setUeType(int ueType) {
		this.ueType = ueType;
	}
	
	public int getMarketCode() {
		return marketCode;
	}
	final public void setMarketCode(int marketCode) {
		this.marketCode = marketCode;
	}
	
	public int getOperatorCode() {
		return operatorCode;
	}
	final public void setOperatorCode(int operatorCode) {
		this.operatorCode = operatorCode;
	}
	
	public int getCellID() {
		return cellID;
	}
	final public void setCellID(int cellID) {
		this.cellID = cellID;
	}
	
	public int getDuration() {
		return duration;
	}
	final public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public int getCauseCode() {
		return causeCode;
	}
	final public void setCauseCode(int causeCode) {
		this.causeCode = causeCode;
	}
	
	public int getNeVersion() {
		return neVersion;
	}
	final public void setNeVersion(int neVersion) {
		this.neVersion = neVersion;
	}
	
	public long getImsiID() {
		return imsiID;
	}
	final public void setImsiID(long imsiID) {
		this.imsiID = imsiID;
	}
	public long getHier_3() {
		return hier_3;
	}
	final public void setHier_3(long hier_3) {
		this.hier_3 = hier_3;
	}
	
	public long getHier_32() {
		return hier_32;
	}
	final public void setHier_32(long hier_32) {
		this.hier_32 = hier_32;
	}
	
	public long getHier_321() {
		return hier_321;
	}
	final public void setHier_321(long hier_321) {
		this.hier_321 = hier_321;
	}
	
	
}
