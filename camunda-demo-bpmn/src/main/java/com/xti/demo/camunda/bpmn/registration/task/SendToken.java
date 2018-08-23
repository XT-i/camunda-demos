package com.xti.demo.camunda.bpmn.registration.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendToken implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendToken.class);

	public static String EMAIL = "email";
	public static String TOKEN = "token";
	
    public void execute(DelegateExecution execution) throws Exception {
    	LOGGER.info("execute - start");
    	
    	String email = (String)execution.getBusinessKey();
    	String token = (String)execution.getVariable(TOKEN);
    	
    	LOGGER.info("sending token '" + token + "' to " + email);

    	LOGGER.info("execute - done");
      
    }

}
