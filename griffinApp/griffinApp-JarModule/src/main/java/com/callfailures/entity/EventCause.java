package com.callfailures.entity;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "eventCause")
@XmlRootElement
@Table(name = "eventCause")
public class EventCause {
	@EmbeddedId
	private EventCausePK eventCauseId;
	
	@OneToMany(fetch=FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name="eventCauseId", referencedColumnName = "eventCauseId",insertable=false,updatable=false),
        @JoinColumn(name="causeCode", referencedColumnName = "causeCode",insertable=false,updatable=false)
		
	}) 
	private List<Events> events;
	
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
