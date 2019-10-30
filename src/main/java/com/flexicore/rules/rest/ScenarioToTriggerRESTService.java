package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.ScenarioToTrigger;
import com.flexicore.rules.request.ScenarioToTriggerCreate;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.rules.request.ScenarioToTriggerUpdate;
import com.flexicore.rules.service.ScenarioToTriggerService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/ScenarioToTrigger")

@Tag(name="Rules")
public class ScenarioToTriggerRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private ScenarioToTriggerService service;

    @POST
    @Produces("application/json")
    @Path("/getAllScenarioToTriggers")
    @Operation(summary = "getAllScenarioToTriggers", description = "get all ScenarioToTriggers, filtered, paged (optionally")
    public PaginationResponse<ScenarioToTrigger> getAllScenarioToTrigger(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "Valid ScenarioToTriggerFilter or empty {} ") ScenarioToTriggerFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllScenarioToTriggers(filter, securityContext);
    }



    @POST
    @Produces("application/json")
    @Path("/createScenarioToTrigger")
    @Operation(summary = "createScenarioToTrigger", description = "create ScenarioToTrigger, practically linking a ScenarioTrigger with a Scenario")
    public ScenarioToTrigger createScenarioToTrigger(
            @HeaderParam("authenticationKey") String authenticationKey,
           @Parameter(description = "A valid ScenarioToTriggerCreate instance ") ScenarioToTriggerCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createScenarioToTrigger(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateScenarioToTrigger")
    @Operation(summary = "updateScenarioToTrigger", description = "Update ScenarioToTrigger, update an exciting link")
    public ScenarioToTrigger updateScenarioToTrigger(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "A valid ScenarioToTriggerUpdate update container") ScenarioToTriggerUpdate scenarioToTriggerUpdate,
            @Context SecurityContext securityContext) {
        ScenarioToTrigger scenarioToTrigger=scenarioToTriggerUpdate.getId()!=null?service.getByIdOrNull(scenarioToTriggerUpdate.getId(),ScenarioToTrigger.class,null,securityContext):null;
        if(scenarioToTrigger==null ){
            throw new BadRequestException("No ScenarioToTrigger with id "+scenarioToTriggerUpdate.getId());
        }
        service.validate(scenarioToTriggerUpdate, securityContext);
        return service.updateScenarioToTrigger(scenarioToTriggerUpdate, securityContext);
    }


}
