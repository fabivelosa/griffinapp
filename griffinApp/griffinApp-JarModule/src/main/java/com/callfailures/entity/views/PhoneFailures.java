package com.callfailures.entity.views;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Immutable;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.UserEquipment;

@Entity(name = "phoneFailures")
@Immutable
public class PhoneFailures {
	
	@Id
	private UserEquipment userEquipment;
	
	private EventCause eventCause;
	
	private long count;

	public PhoneFailures() {
	}

	public PhoneFailures(UserEquipment imsi, EventCause eventCause, long count) {
		this.userEquipment = imsi;
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
