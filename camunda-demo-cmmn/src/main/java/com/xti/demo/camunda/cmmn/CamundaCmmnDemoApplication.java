package com.xti.demo.camunda.cmmn;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.camunda.bpm.spring.boot.starter.event.PreUndeployEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableProcessApplication("myProcessApplicationName")
public class CamundaCmmnDemoApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CamundaCmmnDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CamundaCmmnDemoApplication.class, args);
    }

	@EventListener
	public void onPostDeploy(PostDeployEvent event) {
		
	      CaseService caseService = event.getProcessEngine().getCaseService();
	      CaseInstance caseInstance = caseService.createCaseInstanceByKey("case.servicedesk-laptop-intake", "tag-123",
	          Variables.createVariables()
	            .putValue("type", Variables.stringValue("Macbook"))
	      );
	      
	      CaseInstance caseInstance2 = caseService.createCaseInstanceByKey("case.servicedesk-laptop-intake", "tag-234", 
		          Variables.createVariables()
		            .putValue("type", Variables.stringValue("Dell"))
		      );

	      LOGGER.info("case instance id: " + caseInstance.getId());
	      
		 LOGGER.info("POST DEPLOY ...");
	}

	@EventListener
	public void onPreUndeploy(PreUndeployEvent event) {
		LOGGER.info("PRE UNDEPLOY ...");
	}

}
