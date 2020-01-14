package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.TriggerManager;
import com.flexicore.rules.request.TriggerManagerCreate;
import com.flexicore.rules.request.TriggerManagerFilter;
import com.flexicore.rules.request.TriggerManagerUpdate;
import com.flexicore.rules.service.TriggerManagerService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/TriggerManager")
@Tag(name="Rules")
@Tag(name="TriggerManagers")
public class TriggerManagerRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private TriggerManagerService service;

    @POST
    @Produces("application/json")
    @Path("/getAllTriggerManagers")
    @Operation(summary = "getAllTriggerManagers", description = "get all available TriggerManagers, filtered and paged")
    public PaginationResponse<TriggerManager> getAllTriggerManager(
            @HeaderParam("authenticationKey") String authenticationKey,
            TriggerManagerFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllTriggerManagers(filter, securityContext);
    }



    @POST
    @Produces("application/json")
    @Path("/createTriggerManager")
    @Operation(summary = "createTriggerManager", description = "create a new TriggerManager")
    public TriggerManager createTriggerManager(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "A TriggerManagerCreate Container ") TriggerManagerCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createTriggerManager(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateTriggerManager")
    @Operation(summary = "updateTriggerManager", description = "Update TriggerManager, in a normal workflow, updated once the first top rule is created")
    public TriggerManager updateTriggerManager(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "A TriggerManager Update container",required = true) TriggerManagerUpdate triggerManagerUpdate,
            @Context SecurityContext securityContext) {
        TriggerManager triggerManager=triggerManagerUpdate.getId()!=null?service.getByIdOrNull(triggerManagerUpdate.getId(),TriggerManager.class,null,securityContext):null;
        if(triggerManager==null ){
            throw new BadRequestException("No TriggerManager with id "+triggerManagerUpdate.getId());
        }
        triggerManagerUpdate.setTriggerManager(triggerManager);
        service.validate(triggerManagerUpdate, securityContext);
        return service.updateTriggerManager(triggerManagerUpdate, securityContext);
    }


}
