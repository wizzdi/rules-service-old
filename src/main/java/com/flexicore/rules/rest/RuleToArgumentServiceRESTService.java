package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.RuleToArgument;
import com.flexicore.rules.request.RuleToArgumentCreate;
import com.flexicore.rules.request.RuleToArgumentHolderFilter;
import com.flexicore.rules.request.RuleToArgumentUpdate;

import com.flexicore.rules.service.RuleToArgumentService;
import com.flexicore.security.SecurityContext;
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
@Path("plugins/RuleToArgument")
@Tag(name = "Rules")
@Tag(name="RulesToArgument",description = "RuleToArgument is a M2M link class, it connects A RuleArgument with A Rule." +
        "This allows RuleArguments to be reused with different Rules, see the RuleArgument service and class. ")
public class RuleToArgumentServiceRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private RuleToArgumentService service;

    @POST
    @Produces("application/json")
    @Path("/getAllRulesToExecutionParameter")
    @Operation(summary = "getAllRulesToExecutionParameter", description = "Get all the ExecutionParameters , note that RuleArguments can be reused in different Rules   ")
    public PaginationResponse<RuleToArgument> getAllRulesToExecutionParameter(
            @HeaderParam("authenticationKey") String authenticationKey,
           @RequestBody(description = "filter RuleToArgument, allows filtering by RuleId(s) , find all connections with a certain Rule or Rules") RuleToArgumentHolderFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllRulesToExecutionParameters(filter, securityContext);
    }

    @POST
    @Produces("application/json")
    @Path("/createRuleToArgument")
    @Operation(summary = "createRuleToArgument", description = "Links a Rule to RuleArgument, ")
    public RuleToArgument createRuleToArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
           @RequestBody(description = "A valid RuleToArgumentCreate container") RuleToArgumentCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createRulesToExecutionParameter(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateRuleToArgument")
    @Operation(summary = "updateRuleToArgument", description = "Update RuleToArgument")
    public RuleToArgument updateRuleToArgument(
            @HeaderParam("authenticationKey") String authenticationKey,
            @RequestBody(description = "A valid RuleToArgumentUpdate container") RuleToArgumentUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        RuleToArgument flexiCoreRule=ruleUpdate.getId()!=null?service.getByIdOrNull(ruleUpdate.getId(),RuleToArgument.class,null,securityContext):null;
        if(flexiCoreRule==null ){
            throw new BadRequestException("No RuleToArgument with id "+ruleUpdate.getId());
        }
        service.validate(ruleUpdate, securityContext);
        return service.updateRulesToExecutionParameter(ruleUpdate, securityContext);
    }


}
