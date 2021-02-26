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
	@JoinColumn(name = "tac", referencedColumnName = "tac")
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
	private String imsi;

	@NotNull
	private String hier3Id;

	@NotNull
	private String hier32Id;

	@NotNull
	private String hier321Id;

	public Events() {
	}

	public Events(final EventCause eventCause, final LocalDateTime dateTime, final FailureClass failureClass,
			final UserEquipment ueType, final MarketOperator marketOperator, final int cellId, final int duration,
			final String neVersion, final String imsi, final String hier3Id, final String hier32Id, final String hier321Id) {
		this.eventCause = eventCause;
		this.dateTime = dateTime;
		this.failureClass = failureClass;
		this.ueType = ueType;
		this.marketOperator = marketOperator;
		this.cellId = cellId;
		this.duration = duration;
		this.neVersion = neVersion;
		this.imsi = imsi;
		this.hier3Id = hier3Id;
		this.hier32Id = hier32Id;
		this.hier321Id = hier321Id;
	}

	public UserEquipment getUeType() {
		return ueType;
	}

	public void setUeType(final UserEquipment ueType) {
		this.ueType = ueType;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(final int eventId) {
		this.eventId = eventId;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(final LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public MarketOperator getMarketOperator() {
		return marketOperator;
	}

	public void setMarketOperator(final MarketOperator marketOperator) {
		this.marketOperator = marketOperator;
	}

	public FailureClass getFailureClass() {
		return failureClass;
	}

	public void setFailureClass(final FailureClass failureClass) {
		this.failureClass = failureClass;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(final int cellId) {
		this.cellId = cellId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	public String getNeVersion() {
		return neVersion;
	}

	public void setNeVersion(final String neVersion) {
		this.neVersion = neVersion;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(final String imsi) {
		this.imsi = imsi;
	}

	public String getHier3Id() {
		return hier3Id;
	}

	public void setHier3Id(final String hier3Id) {
		this.hier3Id = hier3Id;
	}

	public String getHier32Id() {
		return hier32Id;
	}

	public void setHier32Id(final String hier32Id) {
		this.hier32Id = hier32Id;
	}

	public String getHier321Id() {
		return hier321Id;
	}

	public void setHier321Id(final String hier321Id) {
		this.hier321Id = hier321Id;
	}

	public EventCause getEventCause() {
		return eventCause;
	}

	public void setEventCause(final EventCause eventCause) {
		this.eventCause = eventCause;
	}
}
