package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.ScenarioTriggerType;
import com.flexicore.rules.request.ScenarioTriggerTypeCreate;
import com.flexicore.rules.request.ScenarioTriggerTypeFilter;
import com.flexicore.rules.request.ScenarioTriggerTypeUpdate;
import com.flexicore.rules.service.ScenarioTriggerTypeService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/ScenarioTriggerType")
@Tag(name = "Rules")
@Tag(name = "ScenarioTriggerType")
@Extension
@Component
public class ScenarioTriggerTypeRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioTriggerTypeService service;

	@POST
	@Produces("application/json")
	@Path("/getAllScenarioTriggerTypes")
	@Operation(summary = "getAllScenarioTriggerTypes", description = "get all ScenarioTriggerTypes, filtered, paged (optionally)")
	public PaginationResponse<ScenarioTriggerType> getAllScenarioTriggerType(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid ScenarioTriggerTypeFilter or an empty one (new instance, {}") ScenarioTriggerTypeFilter filter,
			@Context SecurityContext securityContext) {
		service.validate(filter, securityContext);
		return service.getAllScenarioTriggerTypes(filter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createScenarioTriggerType")
	@Operation(summary = "createScenarioTriggerType", description = "create ScenarioTriggerType instance")
	public ScenarioTriggerType createScenarioTriggerType(
			@HeaderParam("authenticationKey") String authenticationKey,
			ScenarioTriggerTypeCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		return service.createScenarioTriggerType(creationContainer, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateScenarioTriggerType")
	@Operation(summary = "updateScenarioTriggerType", description = "Update ScenarioTriggerType")
	public ScenarioTriggerType updateScenarioTriggerType(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid ScenarioTriggerTypeUpdate instance with a valid Id of an existing ScenarioTriggerType ") ScenarioTriggerTypeUpdate scenarioTriggerTypeUpdate,
			@Context SecurityContext securityContext) {
		ScenarioTriggerType scenarioTriggerType = scenarioTriggerTypeUpdate.getId() != null ? service.getByIdOrNull(scenarioTriggerTypeUpdate.getId(), ScenarioTriggerType.class, null, securityContext) : null;
		if (scenarioTriggerType == null) {
			throw new BadRequestException("No ScenarioTriggerType with id " + scenarioTriggerTypeUpdate.getId());
		}
		scenarioTriggerTypeUpdate.setScenarioTriggerType(scenarioTriggerType);
		service.validate(scenarioTriggerTypeUpdate, securityContext);
		return service.updateScenarioTriggerType(scenarioTriggerTypeUpdate,
				securityContext);
	}

}
