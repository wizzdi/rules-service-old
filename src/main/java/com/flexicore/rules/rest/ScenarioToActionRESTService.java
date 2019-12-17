package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.request.ScenarioToActionCreate;
import com.flexicore.rules.request.ScenarioToActionFilter;
import com.flexicore.rules.request.ScenarioToActionUpdate;
import com.flexicore.rules.service.ScenarioToActionService;
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
@ProtectedREST
@Path("plugins/ScenarioToAction")
@Tag(name="Rules")
@Tag(name="Scenarios")
@Tag(name="Actions")
public class ScenarioToActionRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private ScenarioToActionService service;

    @POST
    @Produces("application/json")
    @Path("/getAllScenarioToActions")
    @Operation(summary = "getAllScenarioToActions", description = "get all ScenarioToActions")
    public PaginationResponse<ScenarioToAction> getAllScenarioToAction(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "Valid ScenarioToActionFilter, " +
                    "can be {} (empty body for all instances," +
                    " using pagination in filters is highly recommended") ScenarioToActionFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllScenarioToActions(filter, securityContext);
    }



    @POST
    @Produces("application/json")
    @Path("/createScenarioToAction")
    @Operation(summary = "createScenarioToAction", description = "create a new ScenarioToAction")
    public ScenarioToAction createScenarioToAction(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "A valid ScenarioToActionCreate including " +
                    "required fields for a new ScenarioToAction") ScenarioToActionCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createScenarioToAction(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateScenarioToAction")
    @Operation(summary = "updateScenarioToAction", description = "Update ScenarioToAction")
    public ScenarioToAction updateScenarioToAction(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "A valid ScenarioToActionUpdate including required" +
                    " fields for a updating ScenarioToAction") ScenarioToActionUpdate scenarioToActionUpdate,
            @Context SecurityContext securityContext) {
        ScenarioToAction scenarioToAction=scenarioToActionUpdate.getId()!=null?service.getByIdOrNull(scenarioToActionUpdate.getId(),ScenarioToAction.class,null,securityContext):null;
        if(scenarioToAction==null ){
            throw new BadRequestException("No ScenarioToAction with id "+scenarioToActionUpdate.getId());
        }
        scenarioToActionUpdate.setScenarioToAction(scenarioToAction);
        service.validate(scenarioToActionUpdate, securityContext);
        return service.updateScenarioToAction(scenarioToActionUpdate, securityContext);
    }


}
