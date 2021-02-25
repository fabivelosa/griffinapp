package com.callfailures.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class MarketOperatorPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(length = 3)
	private int countryCode;

	@NotNull
	@Column(length = 3)
	@Size(min = 2, max = 3)
	private int operatorCode;

	@Override
	public int hashCode() {
		return Objects.hash(countryCode, operatorCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MarketOperatorPK)) {
			return false;
		}
		MarketOperatorPK other = (MarketOperatorPK) obj;
		return countryCode == other.countryCode && operatorCode == other.operatorCode;
	}

}
