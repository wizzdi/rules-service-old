package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.request.ClearLogRequest;
import com.flexicore.rules.request.ScenarioCreate;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.ScenarioUpdate;
import com.flexicore.rules.service.ScenarioService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@Path("plugins/Scenario")
@Tag(name = "Rules")
@Tag(name = "Scenarios")
@Extension
@Component
public class ScenarioRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioService service;

	@POST
	@Produces("application/json")
	@Path("/getAllScenarios")
	@Operation(summary = "getAllScenarios", description = "get all available Scenarios, filtered and paged")
	public PaginationResponse<Scenario> getAllScenario(
			@HeaderParam("authenticationKey") String authenticationKey,
			ScenarioFilter filter, @Context SecurityContext securityContext) {
		service.validate(filter, securityContext);
		return service.getAllScenarios(filter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/clearLog")
	@Operation(summary = "clearLog", description = "Clear Log")
	public void clearLog(
			@HeaderParam("authenticationKey") String authenticationKey,
			@RequestBody(description = "Valid ClearLogRequest container", required = true) ClearLogRequest creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		service.clearLog(creationContainer, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createScenario")
	@Operation(summary = "createScenario", description = "create a new Scenario")
	public Scenario createScenario(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A ScenarioCreate Container ") ScenarioCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		return service.createScenario(creationContainer, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateScenario")
	@Operation(summary = "updateScenario", description = "Update Scenario, in a normal workflow, updated once the first top rule is created")
	public Scenario updateScenario(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A Scenario Update container", required = true) ScenarioUpdate scenarioUpdate,
			@Context SecurityContext securityContext) {
		Scenario scenario = scenarioUpdate.getId() != null ? service
				.getByIdOrNull(scenarioUpdate.getId(), Scenario.class, null,
						securityContext) : null;
		if (scenario == null) {
			throw new BadRequestException("No Scenario with id "
					+ scenarioUpdate.getId());
		}
		scenarioUpdate.setScenario(scenario);
		service.validate(scenarioUpdate, securityContext);
		return service.updateScenario(scenarioUpdate, securityContext);
	}

}
