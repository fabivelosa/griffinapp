package com.callfailures.utils.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.EventCausePK;
import com.callfailures.entity.Events;
import com.callfailures.entity.FailureClass;
import com.callfailures.entity.MarketOperator;
import com.callfailures.entity.MarketOperatorPK;
import com.callfailures.entity.UserEquipment;

public class EntityGenerator {
	
	public Events generateCallFailureInstance(final String localDateTime, final Integer eventId,
			final Integer failureId, final Integer ueId, final Integer mCc, final Integer mNc, final Integer cellId,
			final Integer duration, final Integer causeCode, final String neVersion, final String IMSI,
			final String hier3Id, final String hier32Id, final String hier321Id, final String eventCauseDescription,
			final String failureClassDescription, final String country, final String operator) {
		final LocalDateTime dateTime = parseDateTime(localDateTime);
		final EventCause eventCause = parseEventCause(eventId, causeCode, eventCauseDescription);
		final FailureClass failureClass = parseFailureClass(failureId, failureClassDescription);
		final MarketOperator marketOperator = parseMobileCountryNetworkCode(mCc, mNc, country, operator);
		final UserEquipment ueType = parseUE(ueId);
		
		final Events events = new Events(eventCause, dateTime, failureClass, ueType, marketOperator, cellId, duration, neVersion,
				IMSI, hier3Id, hier32Id, hier321Id);
		
		return events;
	}

	
	public UserEquipment parseUE(final Integer ueId) {
		final UserEquipment userEquipment = ueId == null ? null : new UserEquipment(ueId);
		userEquipment.setModel("G410");
		userEquipment.setVendorName("Mitsubishi");
		userEquipment.setAccessCapability("GSM 1800, GSM 900");
		return userEquipment;
	}

	
	public MarketOperator parseMobileCountryNetworkCode(final Integer mCc, final Integer mNc, final String country,
			final String operator) {
		final MarketOperator mobileCountryNetworkCode = new MarketOperator(new MarketOperatorPK(mCc, mNc), country, operator);
		return mobileCountryNetworkCode;
	}

	
	public FailureClass parseFailureClass(final Integer failureId, final String failureClassDescription) {
		final FailureClass failureClass = new FailureClass(failureId, failureClassDescription);
		return failureClass;
	}

	
	public EventCause parseEventCause(final Integer eventId, final Integer causeCode,
			final String eventCauseDescription) {
		final EventCause eventCause = (eventId == null || causeCode == null) ? null : new EventCause(new EventCausePK(eventId, causeCode), eventCauseDescription);
		return eventCause;
	}

	
	public LocalDateTime parseDateTime(final String localDateTime) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm"); 
		final LocalDateTime dateTime = localDateTime == null ? null : LocalDateTime.parse(localDateTime, formatter);
		return dateTime;
	}

}
