package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.request.ScenarioTriggerCreate;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerUpdate;
import com.flexicore.rules.service.ScenarioTriggerService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/ScenarioTrigger")
@OpenAPIDefinition(
        tags = {@Tag(name = "Rules",description = "Flexicore Rules Service"),
                @Tag(name = "ScenarioTrigger",description = "APIs for handling ScenarioTrigger CRUD" +
                        "Scenario is the top hierarchy object in a the Rules system , ScenarioTrigger" +
                        "is the object triggering Scenario evaluation and if evaluated to true, fires all connected actions. Without confusion" +
                        "a ScenarioAction may itself trigger another Scenario")},
        externalDocs = @ExternalDocumentation(
                description = "instructions for how to use FlexiCore Rules, ScenarioTrigger",
                url = "http:www.wizzdi.com"))
public class ScenarioTriggerRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private ScenarioTriggerService service;

    @POST
    @Produces("application/json")
    @Path("/getAllScenarioTriggers")
    @Operation(summary = "getAllScenarioTriggers", description = "get all ScenarioTriggers, filtered, paged (optionally)")
    public PaginationResponse<ScenarioTrigger> getAllScenarioTrigger(
            @HeaderParam("authenticationKey") String authenticationKey,
            @RequestBody(description = "A valid ScenarioTriggerFilter or an empty one (new instance, {}") ScenarioTriggerFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllScenarioTriggers(filter, securityContext);
    }



    @POST
    @Produces("application/json")
    @Path("/createScenarioTrigger")
    @Operation(summary = "createScenarioTrigger", description = "create ScenarioTrigger instance")
    public ScenarioTrigger createScenarioTrigger(
            @HeaderParam("authenticationKey") String authenticationKey,
            ScenarioTriggerCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createScenarioTrigger(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateScenarioTrigger")
    @Operation(summary = "updateScenarioTrigger", description = "Update ScenarioTrigger")
    public ScenarioTrigger updateScenarioTrigger(
            @HeaderParam("authenticationKey") String authenticationKey,
           @RequestBody(description = "A valid ScenarioTriggerUpdate instance with a valid Id of an existing ScenarioTrigger ") ScenarioTriggerUpdate scenarioTriggerUpdate,
            @Context SecurityContext securityContext) {
        ScenarioTrigger scenarioTrigger=scenarioTriggerUpdate.getId()!=null?service.getByIdOrNull(scenarioTriggerUpdate.getId(),ScenarioTrigger.class,null,securityContext):null;
        if(scenarioTrigger==null ){
            throw new BadRequestException("No ScenarioTrigger with id "+scenarioTriggerUpdate.getId());
        }
        service.validate(scenarioTriggerUpdate, securityContext);
        return service.updateScenarioTrigger(scenarioTriggerUpdate, securityContext);
    }


}
