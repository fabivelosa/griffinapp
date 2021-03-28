package com.callfailures.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class DateConverter {

	public static LocalDateTime convertLongToLocalDateTime(final Long startEpoch) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(startEpoch), TimeZone.getDefault().toZoneId());
}
	
}
