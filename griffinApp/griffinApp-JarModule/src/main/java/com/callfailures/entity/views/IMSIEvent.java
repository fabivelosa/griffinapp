package com.callfailures.entity.views;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import com.callfailures.entity.EventCause;

@Entity(name = "imsiEvent")
@Immutable
public class IMSIEvent {

	@Id
	private String imsi;
	
	//private EventCause ecause;
 
	
	public IMSIEvent() {
		
	}
	
	
	/*
	public IMSIEvent(final String imsi, final EventCause ecause) {
		this.imsi = imsi;
		this.ecause = ecause;
	}
	*/
	public String getImsi() {
		return imsi;
	}

	

	
}
