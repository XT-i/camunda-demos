package com.xti.demo.camunda.cmmn.util;

import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Groups.CAMUNDA_ADMIN;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.FilterService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.filter.Filter;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.persistence.entity.AuthorizationEntity;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordering.DEFAULT_ORDER + 1)
@ConfigurationProperties("camunda.bpm.user")
public class DefaultAdminUserEnginePlugin extends AbstractProcessEnginePlugin {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAdminUserEnginePlugin.class);

    private String name = "admin";
    private String password = "admin";
    
	@Override
	public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super.preInit(processEngineConfiguration);
        if (!processEngineConfiguration.isAuthorizationEnabled()) {
        	LOGGER.info("****************** ENABLING SECURITY !!!");
            processEngineConfiguration.setAuthorizationEnabled(true);
        }
	}


	@Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
    	LOGGER.info("****************** CREATING USERS & GROUPS ***");
    	
        final IdentityService identityService = processEngine.getIdentityService();
        final AuthorizationService authorizationService = processEngine.getAuthorizationService();


        if(name != null) {
            User singleResult = identityService.createUserQuery().userId(name).singleResult();
            if (singleResult != null) {
                return;
            }

            LOGGER.info("Generating user data");

            User user = identityService.newUser(name);
            user.setFirstName(name);
            user.setLastName(name);
            user.setPassword(password);
            user.setEmail(name + "@localhost");
            identityService.saveUser(user);

            createGroup(identityService, CAMUNDA_ADMIN, CAMUNDA_ADMIN, Groups.GROUP_TYPE_SYSTEM);

            identityService.createMembership(name, CAMUNDA_ADMIN);

            // create ADMIN authorizations on all built-in resources
            for (Resource resource : Resources.values()) {
                if(authorizationService.createAuthorizationQuery().groupIdIn(CAMUNDA_ADMIN).resourceType(resource).resourceId(Authorization.ANY).count() == 0) {
                	authorizeGroup(authorizationService, CAMUNDA_ADMIN, resource, Authorization.ANY, Permissions.ALL);
                    //AuthorizationEntity userAdminAuth = new AuthorizationEntity(AUTH_TYPE_GRANT);
/*  
                   userAdminAuth.setGroupId(CAMUNDA_ADMIN);
                    userAdminAuth.setResource(resource);
                    userAdminAuth.setResourceId(Authorization.ANY);
                    userAdminAuth.addPermission(Permissions.ALL);
                    authorizationService.saveAuthorization(userAdminAuth);*/
                }
            }

        }
        
        TaskService taskService = processEngine.getTaskService();
        FilterService filterService = processEngine.getFilterService();
        		

        
        TaskQuery allTasksQuery = taskService.createTaskQuery();
        Filter filter = filterService.newTaskFilter("All tasks");
        filter.setQuery(allTasksQuery);
        
        filterService.saveFilter(filter);

        TaskQuery myTasksQuery = taskService.createTaskQuery().taskAssigneeExpression("${ currentUser() }");
        
        filter = filterService.newTaskFilter("My tasks");
        filter.setQuery(myTasksQuery);
        
        filterService.saveFilter(filter);

        TaskQuery myGroupTasksQuery = taskService.createTaskQuery().taskCandidateGroupInExpression("${ currentUserGroups() }");
        
        filter = filterService.newTaskFilter("My Groups Tasks");
        filter.setQuery(myGroupTasksQuery);
        
        filterService.saveFilter(filter);

}
	
	public void authorizeGroup ( AuthorizationService authorizationService, String groupId, Resource resource, String resourceId, Permissions permission ) {
        AuthorizationEntity a = new AuthorizationEntity(AUTH_TYPE_GRANT);
        a.setGroupId(groupId);
        a.setResource(resource);
        a.setResourceId(resourceId);
        a.addPermission(permission);
        authorizationService.saveAuthorization(a);

	}
	

	public void createUser ( IdentityService identityService, String id, String firstName, String lastName, String email, String password, String initialGroup ) {
        User u = identityService.newUser(id);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setPassword(password);
        u.setEmail(email);
        identityService.saveUser(u);
        identityService.createMembership(id, initialGroup);
	}
	
	public void createGroup ( IdentityService identityService, String name, String displayName, String type ) {
		if(identityService.createGroupQuery().groupId(name).count() == 0) {
			Group group = identityService.newGroup(name);
			group.setName(displayName);
			group.setType(type);
			identityService.saveGroup(group);
		}
	
	}
	
	public void createGroup ( IdentityService identityService, String name, String displayName ) {
		createGroup(identityService, name, displayName, Groups.GROUP_TYPE_WORKFLOW);
	}
	
	public void createGroup ( IdentityService identityService, String name ) {
		createGroup(identityService, name, name);
	}

}