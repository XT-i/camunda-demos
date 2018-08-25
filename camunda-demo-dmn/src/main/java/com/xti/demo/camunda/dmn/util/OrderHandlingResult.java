package com.xti.demo.camunda.dmn.util;

public class OrderHandlingResult extends GenericResult {
	
	private boolean includeGift;
	private String label;

	public OrderHandlingResult ( boolean includeGift, String label ) {
		this.includeGift = includeGift;
		this.label = label;
	}
	
	public boolean isIncludeGift() {
		return includeGift;
	}

	public void setIncludeGift(boolean includeGift) {
		this.includeGift = includeGift;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
