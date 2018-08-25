package com.xti.demo.camunda.dmn;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {CamundaDmnDemoApplication.class})
public class OrderHandlingDecisionTest {

	  @Autowired
	  private ProcessEngine processEngine;

	  @Test
	  @Deployment
	  public void testDecision() {
		assertDecision ( 0, 0, 125, true, "firsttime");
		assertDecision ( 0, 0, 300, true, "firsttime");
		assertDecision ( 1, 50, 200, false, "standard");
		assertDecision ( 1, 50, 300, true, "standard");
		assertDecision ( 1, 1200, 200, true, "longtime");
		assertDecision ( 1, 1200, 300, true, "longtime");
		assertDecision ( 60, 300, 200, true, "shopaholic");
		assertDecision ( 60, 300, 300, true, "shopaholic");
		
	  }

	  private void assertDecision ( int previousOrders, int daysSinceLastOrder, Integer orderValue, boolean expectedGift, String expectedLabel ) {
			Map<String,Object> params = new HashMap<>();
			params.put("previousOrders", previousOrders);
			params.put("daysSinceFirstOrder", daysSinceLastOrder);
			params.put("orderValue", orderValue);

			DmnDecisionResult result = processEngine.getDecisionService().evaluateDecisionByKey("decision_consolidated").variables(params).evaluate();
			
			assertNotNull(result);
			assertEquals(expectedGift, result.getSingleResult().getEntry("gift"));
			assertEquals(expectedLabel, result.getSingleResult().getEntry("customerLabel"));
	  }
	  
}

