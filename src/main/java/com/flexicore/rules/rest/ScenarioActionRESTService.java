package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
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

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/ScenarioAction")
@OpenAPIDefinition(
        tags = {@Tag(name = "Rules",description = "Rules Service"),
                @Tag(name = "ScenarioAction",description = "ScenarioAction API for handling Scenario actions, Scenario actions are invoked when Scenario is evaluated to true and there are ScenarioActions connected to it")},
        externalDocs = @ExternalDocumentation(
                description = "instructions for how to use FlexiCore Rules, ScenarioActions",
                url = "http:www.wizzdi.com"))

public class ScenarioActionRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
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
        ScenarioAction scenarioAction=scenarioActionUpdate.getId()!=null?service.getByIdOrNull(scenarioActionUpdate.getId(),ScenarioAction.class,null,securityContext):null;
        if(scenarioAction==null ){
            throw new BadRequestException("No ScenarioAction with id "+scenarioActionUpdate.getId());
        }
        service.validate(scenarioActionUpdate, securityContext);
        return service.updateScenarioAction(scenarioActionUpdate, securityContext);
    }


}
