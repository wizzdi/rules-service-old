package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.request.ScenarioCreate;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.ScenarioUpdate;
import com.flexicore.rules.service.ScenarioService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/Scenario")
@Tag(name="Rules")
@Tag(name="Scenarios")
public class ScenarioRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private ScenarioService service;

    @POST
    @Produces("application/json")
    @Path("/getAllScenarios")
    @Operation(summary = "getAllScenarios", description = "get all available Scenarios, filtered and paged")
    public PaginationResponse<Scenario> getAllScenario(
            @HeaderParam("authenticationKey") String authenticationKey,
            ScenarioFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllScenarios(filter, securityContext);
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
            @Parameter(description = "A Scenario Update container",required = true) ScenarioUpdate scenarioUpdate,
            @Context SecurityContext securityContext) {
        Scenario scenario=scenarioUpdate.getId()!=null?service.getByIdOrNull(scenarioUpdate.getId(),Scenario.class,null,securityContext):null;
        if(scenario==null ){
            throw new BadRequestException("No Scenario with id "+scenarioUpdate.getId());
        }
        service.validate(scenarioUpdate, securityContext);
        return service.updateScenario(scenarioUpdate, securityContext);
    }


}
