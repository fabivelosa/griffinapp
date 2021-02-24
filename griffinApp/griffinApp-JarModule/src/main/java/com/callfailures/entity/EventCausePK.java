package com.callfailures.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class EventCausePK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private int eventCauseId;

	@NotNull
	private int causeCode; // 4099 4098

	@Override
	public int hashCode() {
		return Objects.hash(causeCode, eventCauseId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EventCausePK)) {
			return false;
		}
		EventCausePK other = (EventCausePK) obj;
		return causeCode == other.causeCode && eventCauseId == other.eventCauseId;
	}

	
}
