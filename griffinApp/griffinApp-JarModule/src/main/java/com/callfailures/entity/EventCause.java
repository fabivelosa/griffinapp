package com.callfailures.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "eventCause")
@XmlRootElement
@Table(name = "eventCause")
public class EventCause {
	@EmbeddedId
	private EventCausePK eventCauseId;

	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventCausePK getEventCauseId() {
		return eventCauseId;
	}

	public void setEventCauseId(EventCausePK eventCauseId) {
		this.eventCauseId = eventCauseId;
	}

}
