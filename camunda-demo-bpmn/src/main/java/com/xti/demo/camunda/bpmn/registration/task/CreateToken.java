package com.xti.demo.camunda.bpmn.registration.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateToken implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckAccount.class);

	public static String TOKEN = "token";
	
    public void execute(DelegateExecution execution) throws Exception {
    	LOGGER.info("execute - start");

    	StringBuffer sb = new StringBuffer();
    	for ( int i = 0; i < 5; i++ ) {
    		sb.append((int)(Math.round(Math.random()*9)));
    	}
    	
    	String token = sb.toString();
    	
    	LOGGER.info("setting token '" + token + "'");

    	execution.setVariable(TOKEN, token);
    	
    	LOGGER.info("execute - done");
      
    }

}
