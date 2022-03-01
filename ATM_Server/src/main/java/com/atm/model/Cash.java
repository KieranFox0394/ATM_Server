package com.atm.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Cash {
	
	@JsonProperty
	private int quantity;
	
	@JsonProperty
	private int denomination;

	public Cash()
	{}
	
	public Cash(int inQuantity, int inDenomination) {
		this.quantity = inQuantity;
		this.denomination = inDenomination;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getDenomination() {
		return denomination;
	}

	public void setDenomination(int denomination) {
		this.denomination = denomination;
	}

	
}
