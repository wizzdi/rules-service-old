package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.ScenarioAction;
import com.flexicore.rules.request.ScenarioActionCreate;
import com.flexicore.rules.request.ScenarioActionFilter;
import com.flexicore.rules.request.ScenarioActionUpdate;
import com.flexicore.rules.service.ScenarioActionService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/ScenarioAction")
@Tag(name = "Rules")
@Tag(name = "Scenarios")
@Tag(name = "Actions")
@Extension
@Component
public class ScenarioActionRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioActionService service;

	@POST
	@Produces("application/json")
	@Path("/getAllScenarioActions")
	@Operation(summary = "getAllScenarioActions", description = "get all available ScenarioActions")
	public PaginationResponse<ScenarioAction> getAllScenarioAction(
			@HeaderParam("authenticationKey") String authenticationKey,
			ScenarioActionFilter filter,
			@Context SecurityContext securityContext) {
		service.validate(filter, securityContext);
		return service.getAllScenarioActions(filter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createScenarioAction")
	@Operation(summary = "createScenarioAction", description = "create a new ScenarioAction")
	public ScenarioAction createScenarioAction(
			@HeaderParam("authenticationKey") String authenticationKey,
			ScenarioActionCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		return service.createScenarioAction(creationContainer, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateScenarioAction")
	@Operation(summary = "updateScenarioAction", description = "Update ScenarioAction")
	public ScenarioAction updateScenarioAction(
			@HeaderParam("authenticationKey") String authenticationKey,
			ScenarioActionUpdate scenarioActionUpdate,
			@Context SecurityContext securityContext) {
		ScenarioAction scenarioAction = scenarioActionUpdate.getId() != null
				? service.getByIdOrNull(scenarioActionUpdate.getId(),
						ScenarioAction.class, null, securityContext) : null;
		if (scenarioAction == null) {
			throw new BadRequestException("No ScenarioAction with id "
					+ scenarioActionUpdate.getId());
		}
		scenarioActionUpdate.setScenarioAction(scenarioAction);
		service.validate(scenarioActionUpdate, securityContext);
		return service.updateScenarioAction(scenarioActionUpdate,
				securityContext);
	}

}
