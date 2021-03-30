package com.callfailures.entity.views;


import com.callfailures.entity.MarketOperator;

public class DeviceCombination {

	
	
	private int cellId;
	
	private MarketOperator marketOperator;

	private long count;
	
	
	public DeviceCombination() {
		
	}
	
	public DeviceCombination(final int cellId,final MarketOperator marketOperator, final long count) {
	
		this.cellId =cellId;
		this.marketOperator = marketOperator;
		this.count = count;
	}

	public long getCount() {
		return count;
	}

	public void setCount(final long count) {
		this.count = count;
	}

	public int getCellId() {
		return cellId;
	}

	public void setCellId(final int cellId) {
		this.cellId = cellId;
	}

	public MarketOperator getMarketOperator() {
		return marketOperator;
	}

	public void setMarketOperator(final MarketOperator marketOperator) {
		this.marketOperator = marketOperator;
	}
	
	
}
