package com.callfailures.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "mccmnc")
@XmlRootElement
@Table(name = "mccmnc")
public class MarketOperator {

	@EmbeddedId
	private MarketOperatorPK marketOperatorId;

	
	private String countryDesc;
	private String operatorDesc;

	public MarketOperatorPK getMarketOperatorId() {
		return marketOperatorId;
	}

	public void setMarketOperatorId(MarketOperatorPK marketOperatorId) {
		this.marketOperatorId = marketOperatorId;
	}

	public String getCountryDesc() {
		return countryDesc;
	}

	public void setCountryDesc(String countryDesc) {
		this.countryDesc = countryDesc;
	}

	public String getOperatorDesc() {
		return operatorDesc;
	}

	public void setOperatorDesc(String operatorDesc) {
		this.operatorDesc = operatorDesc;
	}

}
