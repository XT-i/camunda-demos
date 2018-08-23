package com.xti.demo.camunda.bpmn.registration.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Produces;

import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xti.demo.camunda.bpmn.util.GenericResult;

/**
 * DISCLAIMER:
 * This is a simple API to easily demonstrate how Camunda BPM can be intergrated in a Spring Boot based Microservice with REST API
 * API Design is quite awful (all GET with URL path variables, ...) but defined like this to easily demonstrate during a demo  
 */
@Controller
@RequestMapping("/registration")
@Produces("application/json")
public class RegistrationController {

	@Autowired
	RuntimeService runtimeService;

    @GetMapping("/register/{email}/{name}/{phone}")
    public ResponseEntity<GenericResult> register(@PathVariable(name="email") String email, @PathVariable(name="name") String name, @PathVariable(name="phone") String phone, Model model) {
    	Map<String,Object> params = new HashMap<>();
    	params.put("name", name);
    	params.put("phone", phone);
    	runtimeService.startProcessInstanceByMessage("msg.account-requested", email, params );
    	return new ResponseEntity<GenericResult>(new GenericResult(), HttpStatus.OK);
    }

    @GetMapping("/verify-token/{email}/{token}")
    public ResponseEntity<GenericResult> verify(@PathVariable(name="email") String email, @PathVariable(name="token") String token, Model model) {
    	ResponseEntity<GenericResult> response = null;
    	Map<String,Object> params = new HashMap<>();
    	params.put("receivedToken", token);
    	try {
			runtimeService.correlateMessage("msg.token-input", email, params);
	    	response = new ResponseEntity<GenericResult>(new GenericResult(), HttpStatus.OK);
		} catch (MismatchingMessageCorrelationException e) {
	    	response = new ResponseEntity<GenericResult>(new GenericResult(true), HttpStatus.BAD_REQUEST);
		}
    	return response;
    }

}