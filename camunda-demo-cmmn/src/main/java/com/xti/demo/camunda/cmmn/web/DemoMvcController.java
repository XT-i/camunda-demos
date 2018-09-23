package com.xti.demo.camunda.cmmn.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricCaseActivityInstance;
import org.camunda.bpm.engine.history.HistoricCaseInstance;
import org.camunda.bpm.engine.variable.Variables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * DISCLAIMER: very quick-and-dirty demo code!
 */
@Controller
public class DemoMvcController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DemoMvcController.class);

	@Autowired
	CaseService caseService;
	
	@Autowired
	HistoryService historyService;
	
	@Autowired
	TaskService taskService;
	
	@GetMapping("/")
	public String root() {
		return "redirect:home";
	}

	@GetMapping("/home")
	public String homePage(Model model) {
		model.addAttribute("appName", "test app");
		return showHome ( model );
	}
	
	@PostMapping("/open-case")
	public String openCase ( OpenCaseForm openCaseForm, Model model ) {
	  caseService.createCaseInstanceByKey("case.servicedesk-laptop-intake", openCaseForm.getTag(),
	      Variables.createVariables()
	        .putValue("type", openCaseForm.getType()));

		return "redirect:";
	}
	
	@GetMapping("/case/{tag}")
	public String viewCase ( @PathVariable String tag, Model model ) {
		return showCase(model, tag);
	}
	
	@GetMapping("/case/{tag}/plan/{activity}")
	public String manualStartActivity ( @PathVariable String tag, @PathVariable String activity, Model model ) {
		
		 List<HistoricCaseInstance> cases = historyService.createHistoricCaseInstanceQuery().caseDefinitionKey("case.servicedesk-laptop-intake").caseInstanceBusinessKey(tag).list();
		 
		 HistoricCaseInstance c = cases.iterator().next();
    	List<HistoricCaseActivityInstance> enabledActivities = historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(c.getId()).enabled().list();
		for ( HistoricCaseActivityInstance i: enabledActivities ) {
			if (activity.equals(i.getCaseActivityId())) {
				caseService.manuallyStartCaseExecution(i.getCaseExecutionId());
				break;
			}
		}

		return "redirect:/case/" + tag;
	}

	@GetMapping("/case/{tag}/close")
	public String closeCase ( @PathVariable String tag ) {
		 List<HistoricCaseInstance> cases = historyService.createHistoricCaseInstanceQuery().caseDefinitionKey("case.servicedesk-laptop-intake").caseInstanceBusinessKey(tag).list();
		 HistoricCaseInstance c = cases.iterator().next();

		caseService.closeCaseInstance(c.getId());

		return "redirect:/case/" + tag;
	}	

	@GetMapping("/case/{tag}/done/{activity}/{option}")
	public String flagActivityDone ( @PathVariable String tag, @PathVariable String activity, @PathVariable String option, Model model ) {
		
		LOGGER.info("-----" + tag + ", " + activity + ", " + option);
		 List<HistoricCaseInstance> cases = historyService.createHistoricCaseInstanceQuery().caseDefinitionKey("case.servicedesk-laptop-intake").caseInstanceBusinessKey(tag).list();
		 
		 HistoricCaseInstance c = cases.iterator().next();
    	List<HistoricCaseActivityInstance> enabledActivities = historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(c.getId()).active().list();
		for ( HistoricCaseActivityInstance i: enabledActivities ) {
			if (activity.equals(i.getCaseActivityId())) {
				if ( "activity.inspect-laptop-condition".equals(activity )) {
					LOGGER.info("FIRST");
					Map<String,Object> variables = new HashMap<>();
					variables.put("evaluation", option);
					taskService.complete(i.getTaskId(), variables);
				} else {
					LOGGER.info("SECOND");
					taskService.complete(i.getTaskId());
				}
				break;
			}
		}

		return "redirect:/case/" + tag;
	}

	public String showHome ( Model model ) {
		 List<HistoricCaseInstance> openCases = historyService.createHistoricCaseInstanceQuery().caseDefinitionKey("case.servicedesk-laptop-intake").active().list();
		 model.addAttribute("openCases", openCases);
		 		 
		 List<HistoricCaseInstance> closedCases = historyService.createHistoricCaseInstanceQuery().caseDefinitionKey("case.servicedesk-laptop-intake").completed().list();
		 model.addAttribute("closedCases", closedCases);

		 model.addAttribute("openCaseForm", new OpenCaseForm());
		 
		 return "home";		 
	}
	 
	public String showCase ( Model model, String tag ) {
		
		List<HistoricCaseInstance> hcis = historyService.createHistoricCaseInstanceQuery().caseDefinitionKey("case.servicedesk-laptop-intake").caseInstanceBusinessKey(tag).list();
		HistoricCaseInstance hci = hcis.iterator().next(); 
		
		String type = (String)caseService.getVariableLocal(hci.getId(), "type");
		
    	List<HistoricCaseActivityInstance> activeActivities = historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(hci.getId()).active().list();
    	List<HistoricCaseActivityInstance> enabledActivities = historyService.createHistoricCaseActivityInstanceQuery().caseInstanceId(hci.getId()).enabled().list();
		
		CaseForm f = new CaseForm();
		f.setTag(tag);
		f.setType(type);
		
		model.addAttribute("case", f);
		
		for ( HistoricCaseActivityInstance a: activeActivities ) {
			
			if ("humanTask".equals(a.getCaseActivityType())){
				
				ActivityInfo ai = new ActivityInfo();
				ai.setId(a.getCaseActivityId());
				ai.setType(a.getCaseActivityType());
				ai.setDescription(a.getCaseActivityName());
					
				switch ( a.getCaseActivityId() ) {
					case "activity.inspect-laptop-condition":
						ai.getOptions().add("outdated");
						ai.getOptions().add("broken");
						ai.getOptions().add("reusable");
						break;
					default:
						ai.getOptions().add("done");
						
				}
				f.getActivities().add(ai);
			}
		}
		
		for ( HistoricCaseActivityInstance a: enabledActivities ) {
			
			if ("humanTask".equals(a.getCaseActivityType())){
				
				ActivityInfo ai = new ActivityInfo();
				ai.setId(a.getCaseActivityId());
				ai.setType(a.getCaseActivityType());
				ai.setDescription(a.getCaseActivityName());
				f.getOptionals().add(ai);
			}
		}

		return "case";
	}
	
}