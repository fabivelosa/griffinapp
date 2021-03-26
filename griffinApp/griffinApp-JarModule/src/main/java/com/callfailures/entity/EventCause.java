package com.callfailures.entity;

import javax.persistence.Cacheable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "eventCause")
@XmlRootElement
@Table(name = "eventCause")
@Cacheable
public class EventCause {
	
	@EmbeddedId
	@Valid
	private EventCausePK eventCauseId;

	private String description;

	public EventCause() {
	}

	public EventCause(final EventCausePK eventCauseId, final String description) {
		this.eventCauseId = eventCauseId;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public EventCausePK getEventCauseId() {
		return eventCauseId;
	}

	public void setEventCauseId(final EventCausePK eventCauseId) {
		this.eventCauseId = eventCauseId;
	}

}
