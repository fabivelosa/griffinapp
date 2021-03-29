package com.callfailures.entity.views;

import com.callfailures.entity.EventCause;
import com.callfailures.entity.MarketOperator;

public class DeviceCombination {

	
	
	private int cellId;
	
	private MarketOperator marketOperator;

	private long count;
	
	
	public DeviceCombination() {
		
	}
	
	public DeviceCombination( int cellId, MarketOperator marketOperator, final long count) {
	
		this.cellId =cellId;
		this.marketOperator = marketOperator;
		this.count = count;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
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
