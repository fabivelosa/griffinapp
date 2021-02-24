package com.callfailures.entity;

import java.util.Date;

import javax.persistence.Column;
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
	private Date dateTime;

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

	private int cellId;
	private int duration;
	private int causeCode;
	private int neVersion;

	@Column(length = 15)
	private String IMSI;
	private String hier3Id;
	private String hier32Id;
	private String hier321Id;

	
	public Events() {}
	
	public Events(EventCause eventCause, Date dateTime, FailureClass failureClass, UserEquipment ueType,
			MarketOperator marketOperator, int cellId, int duration, int causeCode, int neVersion, String iMSI,
			String hier3Id, String hier32Id, String hier321Id) {
		this.eventCause = eventCause;
		this.dateTime = dateTime;
		this.failureClass = failureClass;
		this.ueType = ueType;
		this.marketOperator = marketOperator;
		this.cellId = cellId;
		this.duration = duration;
		this.causeCode = causeCode;
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

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
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

	public int getCauseCode() {
		return causeCode;
	}

	public void setCauseCode(int causeCode) {
		this.causeCode = causeCode;
	}

	public int getNeVersion() {
		return neVersion;
	}

	public void setNeVersion(int neVersion) {
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
