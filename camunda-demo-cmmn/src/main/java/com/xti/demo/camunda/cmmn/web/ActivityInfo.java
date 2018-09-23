package com.xti.demo.camunda.cmmn.web;

import java.util.ArrayList;
import java.util.List;

public class ActivityInfo {

	private String id;
	private String type;
	private String description;
	private List<String> options = new ArrayList<>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<String> getOptions() {
		return options;
	}
	public void setOptions(List<String> options) {
		this.options = options;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
