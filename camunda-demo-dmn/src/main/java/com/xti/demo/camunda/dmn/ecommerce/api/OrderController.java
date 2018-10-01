package com.xti.demo.camunda.dmn.ecommerce.api;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.engine.DecisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xti.demo.camunda.dmn.util.GenericResult;
import com.xti.demo.camunda.dmn.util.OrderHandlingResult;

/**
 * DISCLAIMER:
 * This is a simple API to easily demonstrate how Camunda BPM can be intergrated in a Spring Boot based Microservice with REST API
 * API Design is quite awful (all GET with URL path variables, ...) but defined like this to easily demonstrate during a demo  
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	DecisionService decisionService;

    @GetMapping("/determine-handling/{previousOrders}/{daysSinceFirstOrder}/{valueOfOrder}")
    public GenericResult register(@PathVariable(name="previousOrders") Integer previousOrders, @PathVariable(name="daysSinceFirstOrder") Integer daysSinceFirstOrder, @PathVariable(name="valueOfOrder") Integer valueOfOrder, Model model) {
    	Map<String,Object> params = new HashMap<>();
    	params.put("previousOrders", previousOrders);
    	params.put("daysSinceFirstOrder", daysSinceFirstOrder);
    	params.put("orderValue", valueOfOrder);
    	
    	DmnDecisionResult result = decisionService.evaluateDecisionByKey("decision_consolidated").variables(params).evaluate();
    	
    	LOGGER.info("" + result);
    	
    	Boolean gift = result.getFirstResult().getEntry("gift");
    	String label = result.getFirstResult().getEntry("customerLabel");
    	
    	return new OrderHandlingResult(gift, label);
    }

}