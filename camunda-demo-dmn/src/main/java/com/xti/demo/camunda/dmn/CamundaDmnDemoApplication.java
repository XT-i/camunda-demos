package com.xti.demo.camunda.dmn;

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
public class CamundaDmnDemoApplication {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CamundaDmnDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CamundaDmnDemoApplication.class, args);
    }

	@EventListener
	public void onPostDeploy(PostDeployEvent event) {
		 LOGGER.info("POST DEPLOY ...");
	}

	@EventListener
	public void onPreUndeploy(PreUndeployEvent event) {
		LOGGER.info("PRE UNDEPLOY ...");
	}

}
