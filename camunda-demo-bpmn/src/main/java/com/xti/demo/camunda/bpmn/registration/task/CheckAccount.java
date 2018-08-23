package com.xti.demo.camunda.bpmn.registration.task;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckAccount implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckAccount.class);

	public static String EMAIL = "email";
	
	public static String ACCOUNT_KNOWN = "accountKnown";
	public static String ACCOUNT_ACTIVATED = "accountActivated";
	
    public void execute(DelegateExecution execution) throws Exception {
    	LOGGER.info("execute - start");
    	
    	String email = (String) execution.getBusinessKey();
    	
    	LOGGER.info("checking user '" + email + "'");

    	if ( "test@test.be".equalsIgnoreCase(email )) {
        	execution.setVariable(ACCOUNT_KNOWN, true);
        	execution.setVariable(ACCOUNT_ACTIVATED, true);
    	} else {
        	execution.setVariable(ACCOUNT_KNOWN, false);
        	execution.setVariable(ACCOUNT_ACTIVATED, false);
    	}
    	
    	LOGGER.info("execute - done");
      
    }

}