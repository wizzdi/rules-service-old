package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.FlexiCoreRuleArgument;
import com.flexicore.rules.request.RuleArgumentCreate;
import com.flexicore.rules.request.RuleArgumentFilter;
import com.flexicore.rules.request.RuleArgumentUpdate;
import com.flexicore.rules.service.RuleArgumentService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/RuleArgument")
@Tag(name="RuleArgument")

public class RuleArgumetRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private RuleArgumentService service;

    @POST
    @Produces("application/json")
    @Path("/getAllRuleArgument")
    @Operation(summary = "getAllRuleArgument", description = "getAllRuleArgument")
    public PaginationResponse<FlexiCoreRuleArgument> getAllRuleArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleArgumentFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllRuleArguments(filter, securityContext);
    }



    @POST
    @Produces("application/json")
    @Path("/createRuleArgument")
    @Operation(summary = "createRuleArgument", description = "create RuleArgument")
    public FlexiCoreRuleArgument createRuleArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleArgumentCreate creationContainer,
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
            RuleArgumentUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRuleArgument flexiCoreRuleArgument=ruleUpdate.getId()!=null?service.getByIdOrNull(ruleUpdate.getId(),FlexiCoreRuleArgument.class,null,securityContext):null;
        if(flexiCoreRuleArgument==null ){
            throw new BadRequestException("No Rule with id "+ruleUpdate.getId());
        }
        service.validate(ruleUpdate, securityContext);
        return service.updateRuleArgument(ruleUpdate, securityContext);
    }


}
