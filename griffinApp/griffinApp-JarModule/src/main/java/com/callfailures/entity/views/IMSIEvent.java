package com.callfailures.entity.views;

import com.callfailures.entity.EventCause;


public class IMSIEvent {

	
	private String imsi;
	
	private EventCause eventCause;
 
	
	public IMSIEvent() {
		
	}
	
	public IMSIEvent(final String imsi, final EventCause eventCause) {
		this.imsi = imsi;
		this.eventCause = eventCause;
	}
	
	public String getImsi() {
		return imsi;
	}

	public EventCause getEventCause() {
		return eventCause;
	}


	
}
