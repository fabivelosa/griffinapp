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
	private int causeCode;

	public EventCausePK() {
	}

	public EventCausePK(final int eventCauseId, final int causeCode) {
		this.eventCauseId = eventCauseId;
		this.causeCode = causeCode;
	}

	public int getEventCauseId() {
		return eventCauseId;
	}

	public void setEventCauseId(int eventCauseId) {
		this.eventCauseId = eventCauseId;
	}

	public int getCauseCode() {
		return causeCode;
	}

	public void setCauseCode(int causeCode) {
		this.causeCode = causeCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(causeCode, eventCauseId);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EventCausePK)) {
			return false;
		}
		final EventCausePK other = (EventCausePK) obj;
		return causeCode == other.causeCode && eventCauseId == other.eventCauseId;
	}

}
