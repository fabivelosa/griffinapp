package com.callfailures.entity.views;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.MarketOperator;

public class DeviceCombination {

	
	private EventCause eventCause;
	
	private int cellId;
	
	private MarketOperator marketOperator;

	
	public DeviceCombination() {
		
	}
	
	public DeviceCombination(EventCause eventCause, int cellId, MarketOperator marketOperator ) {
		this.eventCause = eventCause;
		this.cellId =cellId;
		this.marketOperator = marketOperator;
	}

	public EventCause getEventCause() {
		return eventCause;
	}

	public void setEventCause(EventCause eventCause) {
		this.eventCause = eventCause;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(int cellId) {
		this.cellId = cellId;
	}

	public MarketOperator getMarketOperator() {
		return marketOperator;
	}

	public void setMarketOperator(MarketOperator marketOperator) {
		this.marketOperator = marketOperator;
	}
	
	
}
