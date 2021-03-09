package com.callfailures.entity.views;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

@Entity(name = "imsiEvent")
@Immutable
public class IMSIEvent {

	@Id
	private String imsi;
	
	private long eventID;
	
	private long causeCode;

	
	public IMSIEvent() {
		
	}
	
	public IMSIEvent(final String imsi, final long eventID, final long causeCode) {
		this.imsi = imsi;
		this.eventID = eventID;
		this.causeCode = causeCode;
	}
	
}
