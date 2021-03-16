package com.callfailures.entity.views;


import com.callfailures.entity.EventCause;
import com.callfailures.entity.UserEquipment;

public class PhoneFailures {
	
	private UserEquipment userEquipment;
	
	private EventCause eventCause;
	
	private long count;

	public PhoneFailures() {
	}

	public PhoneFailures(final UserEquipment userEquipment, final EventCause eventCause, final long count) {
		this.userEquipment = userEquipment;
		this.eventCause = eventCause;
		this.count = count;
	}

	public UserEquipment getUserEquipment() {
		return userEquipment;
	}

	public EventCause getEventCause() {
		return eventCause;
	}

	public long getCount() {
		return count;
	}
}
