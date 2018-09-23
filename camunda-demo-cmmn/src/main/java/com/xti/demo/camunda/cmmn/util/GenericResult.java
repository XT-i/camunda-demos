package com.xti.demo.camunda.cmmn.util;

public class GenericResult {

	public static final String OK = "OK";
	public static final String ERROR = "ERROR";
	
	private String code = OK;
	
	public GenericResult ( ) {
		this(false);
	}
	
	public GenericResult ( boolean error ) {
		this.code = error ? ERROR: OK;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
