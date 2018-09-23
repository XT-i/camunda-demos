package com.xti.demo.camunda.cmmn.util;

import java.util.ArrayList;
import java.util.List;

public class CaseDetailsResult {
	
	private boolean open;
	private List<CaseDetailsResultItem> itemsActive = new ArrayList<>();
	private List<CaseDetailsResultItem> itemsAvailable = new ArrayList<>();
	private List<CaseDetailsResultItem> itemsCompleted = new ArrayList<>();
	private List<CaseDetailsResultItem> itemsTerminated = new ArrayList<>();
	private List<CaseDetailsResultItem> itemsEnabled = new ArrayList<>();
	

	private List<CaseDetailsResultItem> itemsAll = new ArrayList<>();

	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public List<CaseDetailsResultItem> getItemsActive() {
		return itemsActive;
	}
	public void setItemsActive(List<CaseDetailsResultItem> itemsActive) {
		this.itemsActive = itemsActive;
	}
	public List<CaseDetailsResultItem> getItemsAvailable() {
		return itemsAvailable;
	}
	public void setItemsAvailable(List<CaseDetailsResultItem> itemsAvailable) {
		this.itemsAvailable = itemsAvailable;
	}
	public List<CaseDetailsResultItem> getItemsCompleted() {
		return itemsCompleted;
	}
	public void setItemsCompleted(List<CaseDetailsResultItem> itemsCompleted) {
		this.itemsCompleted = itemsCompleted;
	}
	public List<CaseDetailsResultItem> getItemsTerminated() {
		return itemsTerminated;
	}
	public void setItemsTerminated(List<CaseDetailsResultItem> itemsTerminated) {
		this.itemsTerminated = itemsTerminated;
	}
	public List<CaseDetailsResultItem> getItemsEnabled() {
		return itemsEnabled;
	}
	public void setItemsEnabled(List<CaseDetailsResultItem> itemsEnabled) {
		this.itemsEnabled = itemsEnabled;
	}
	public List<CaseDetailsResultItem> getItemsAll() {
		return itemsAll;
	}
	public void setItemsAll(List<CaseDetailsResultItem> itemsAll) {
		this.itemsAll = itemsAll;
	}
	
}
