package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.FlexiCoreRuleArgument;
import com.flexicore.rules.request.RuleArgumentCreate;
import com.flexicore.rules.request.RuleArgumentFilter;
import com.flexicore.rules.request.RuleArgumentUpdate;
import com.flexicore.rules.service.RuleArgumentService;
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
@Path("plugins/RuleArgument")
@Tag(name = "Rules")
@Tag(name = "Rules Arguments")

public class RuleArgumentRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private RuleArgumentService service;

    //        @Parameter(description = " an empty object {} or fully/partially filled filter")
    @POST
    @Produces("application/json")
    @Path("/getAllRuleArgument")
    @Operation(summary = "getAllRuleArgument", description = "Returns a paginated filtered sorted (optionally) list of RuleArguments")

    public PaginationResponse<FlexiCoreRuleArgument> getAllRuleArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleArgumentFilter filter,
            @Parameter(name = "SecurityContext", description = "Must be last in parameters and injected by the system") @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllRuleArguments(filter, securityContext);
    }


    @POST
    @Produces("application/json")
    @Path("/createRuleArgument")
    @Operation(summary = "createRuleArgument", description = "creates a RuleArgument" +
            "a RuleArgument uses the system generic dynamic execution to return a collection of values at run time." +
            "The argument defines a list of SubParameters providing a filter for returning the desired collection")
    public FlexiCoreRuleArgument createRuleArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "Properly created RuleArgumentCreate container") RuleArgumentCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createRuleArgument(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateRuleArgument")
    @Operation(summary = "updateRuleArgument", description = "Update RuleArgument")
    public FlexiCoreRuleArgument updateRuleArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
            @Parameter(description = "Properly set RuleArgumentUpdate instance") RuleArgumentUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRuleArgument flexiCoreRuleArgument = ruleUpdate.getId() != null ? service.getByIdOrNull(ruleUpdate.getId(), FlexiCoreRuleArgument.class, null, securityContext) : null;
        if (flexiCoreRuleArgument == null) {
            throw new BadRequestException("No Rule with id " + ruleUpdate.getId());
        }
        ruleUpdate.setFlexiCoreRuleArgument(flexiCoreRuleArgument);
        service.validate(ruleUpdate, securityContext);
        return service.updateRuleArgument(ruleUpdate, securityContext);
    }


}
