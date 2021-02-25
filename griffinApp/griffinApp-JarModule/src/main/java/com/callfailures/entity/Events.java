package com.callfailures.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "event")
@XmlRootElement
@Table(name = "event")
public class Events {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int eventId;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "eventCauseId", referencedColumnName = "eventCauseId", insertable = false, updatable = false),
			@JoinColumn(name = "causeCode", referencedColumnName = "causeCode", insertable = false, updatable = false)

	})
	@NotNull
	private EventCause eventCause;

	@NotNull
	private LocalDateTime dateTime;

	@ManyToOne
	@JoinColumn(name = "failureClass", referencedColumnName = "failureClass")
	@NotNull
	private FailureClass failureClass;

	@ManyToOne
	@JoinColumn(name = "ueType", referencedColumnName = "ueType")
	@NotNull
	private UserEquipment ueType;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "countryCode", referencedColumnName = "countryCode", insertable = false, updatable = false),
			@JoinColumn(name = "operatorCode", referencedColumnName = "operatorCode", insertable = false, updatable = false)

	})
	@NotNull
	private MarketOperator marketOperator;

	@NotNull
	private int cellId;
	
	@NotNull
	private int duration;
	
	@NotNull
	private String neVersion;

	@NotNull
	private String IMSI;
	
	@NotNull
	private String hier3Id;
	
	@NotNull
	private String hier32Id;
	
	@NotNull
	private String hier321Id;

	
	public Events() {}
	
	public Events(EventCause eventCause, LocalDateTime dateTime, FailureClass failureClass, UserEquipment ueType,
			MarketOperator marketOperator, int cellId, int duration, String neVersion, String iMSI,
			String hier3Id, String hier32Id, String hier321Id) {
		this.eventCause = eventCause;
		this.dateTime = dateTime;
		this.failureClass = failureClass;
		this.ueType = ueType;
		this.marketOperator = marketOperator;
		this.cellId = cellId;
		this.duration = duration;
		this.neVersion = neVersion;
		IMSI = iMSI;
		this.hier3Id = hier3Id;
		this.hier32Id = hier32Id;
		this.hier321Id = hier321Id;
	}

	public UserEquipment getUeType() {
		return ueType;
	}

	public void setUeType(UserEquipment ueType) {
		this.ueType = ueType;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public MarketOperator getMarketOperator() {
		return marketOperator;
	}

	public void setMarketOperator(MarketOperator marketOperator) {
		this.marketOperator = marketOperator;
	}

	public FailureClass getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(FailureClass failureClass) {
		this.failureClass = failureClass;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getNeVersion() {
		return neVersion;
	}

	public void setNeVersion(String neVersion) {
		this.neVersion = neVersion;
	}

	public String getIMSI() {
		return IMSI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public String getHier3Id() {
		return hier3Id;
	}

	public void setHier3Id(String hier3Id) {
		this.hier3Id = hier3Id;
	}

	public String getHier32Id() {
		return hier32Id;
	}

	public void setHier32Id(String hier32Id) {
		this.hier32Id = hier32Id;
	}

	public String getHier321Id() {
		return hier321Id;
	}

	public void setHier321Id(String hier321Id) {
		this.hier321Id = hier321Id;
	}

	public EventCause getEventCause() {
		return eventCause;
	}

	public void setEventCause(EventCause eventCause) {
		this.eventCause = eventCause;
	}
}
