package com.xti.demo.camunda.cmmn.web;

import java.util.ArrayList;
import java.util.List;

public class CaseForm {
	
	private String tag;
	private String type;
	private List<ActivityInfo> activities = new ArrayList<>();
	private List<ActivityInfo> optionals = new ArrayList<>();
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<ActivityInfo> getActivities() {
		return activities;
	}
	public void setActivities(List<ActivityInfo> activities) {
		this.activities = activities;
	}
	public List<ActivityInfo> getOptionals() {
		return optionals;
	}
	public void setOptionals(List<ActivityInfo> optionals) {
		this.optionals = optionals;
	}

}
