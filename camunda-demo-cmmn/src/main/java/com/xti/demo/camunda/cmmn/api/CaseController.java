package com.xti.demo.camunda.cmmn.api;

import java.util.List;

import javax.ws.rs.Produces;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricCaseActivityInstance;
import org.camunda.bpm.engine.history.HistoricCaseActivityInstanceQuery;
import org.camunda.bpm.engine.history.HistoricCaseInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xti.demo.camunda.cmmn.util.CaseDetailsResult;
import com.xti.demo.camunda.cmmn.util.CaseDetailsResultItem;

/**
 * DISCLAIMER:
 * This is a simple API to easily demonstrate how Camunda BPM can be intergrated in a Spring Boot based Microservice with REST API
 * API Design is quite awful (all GET with URL path variables, ...) but defined like this to easily demonstrate during a demo  
 */
@Controller
@RequestMapping("/api/case")
@Produces("application/json")
public class CaseController {

	@Autowired
	CaseService caseService;

	@Autowired
	HistoryService historyService;

	@GetMapping("/{caseId}")
    public ResponseEntity<CaseDetailsResult> register(@PathVariable(name="caseId") String caseId, Model model) {
    	CaseDetailsResult result = new CaseDetailsResult();

    	HistoricCaseInstance caseInstance = historyService.createHistoricCaseInstanceQuery().caseInstanceId(caseId).singleResult();
    	
    	result.setOpen(caseInstance.isActive());
    	
    	mapItems(result.getItemsActive(), historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(caseId).active().list());
    	mapItems(result.getItemsAvailable(), historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(caseId).available().list());
    	mapItems(result.getItemsCompleted(), historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(caseId).completed().list());
    	mapItems(result.getItemsTerminated(), historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(caseId).terminated().list());
    	mapItems(result.getItemsEnabled(), historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(caseId).enabled().list());
    	
    	mapItems(result.getItemsAll(), historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(caseId).list());

    	return new ResponseEntity<CaseDetailsResult>(result, HttpStatus.OK);
    }
	
	private void mapItems ( List<CaseDetailsResultItem> target ,List<HistoricCaseActivityInstance> source ) {
		for ( HistoricCaseActivityInstance item: source ) {
			CaseDetailsResultItem i = new CaseDetailsResultItem();
			i.setName(item.getCaseActivityName());
			i.setType(item.getCaseActivityType());
			target.add(i);
		}
	}

}