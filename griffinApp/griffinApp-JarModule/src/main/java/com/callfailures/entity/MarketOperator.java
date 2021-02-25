package com.callfailures.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity(name = "mccmnc")
@XmlRootElement
@Table(name = "mccmnc")
public class MarketOperator {

	@EmbeddedId
	@Valid
	private MarketOperatorPK marketOperatorId;
	
	@NotNull
	private String countryDesc;
	
	@NotNull
	private String operatorDesc;

	public MarketOperator() {
		
	}
	
	public MarketOperator(MarketOperatorPK marketOperatorId, String countryDesc, String operatorDesc) {
		this.marketOperatorId = marketOperatorId;
		this.countryDesc = countryDesc;
		this.operatorDesc = operatorDesc;
	}

	
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
