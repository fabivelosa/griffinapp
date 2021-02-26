package com.callfailures.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
public class MarketOperatorPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Min(100)
	@Max(999)
	private int countryCode;

	@NotNull
	@Min(0)
	@Max(999)
	private int operatorCode;

	public MarketOperatorPK() {
	}

	public MarketOperatorPK(final int countryCode, final int operatorCode) {
		this.countryCode = countryCode;
		this.operatorCode = operatorCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(countryCode, operatorCode);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MarketOperatorPK)) {
			return false;
		}
		final MarketOperatorPK other = (MarketOperatorPK) obj;
		return countryCode == other.countryCode && operatorCode == other.operatorCode;
	}

}
