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
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@Interceptors({SecurityImposer.class, DynamicResourceInjector.class})
@Path("plugins/RuleToArgument")
@OpenAPIDefinition(
        tags = {@Tag(name = "Rules",description = "Rules Service, the rules service provides required API to" +
                "create scenarios containing hierarchical rules, the rules in a scenario describe an expression" +
                "the expression consists of OR,AND,NOT and JavaScript snippet" +
                "The Javascript is provided with arguments evaluated by the server in run time." +
                "a Scenario evaluates to true or false, when true, it may fire ScenarioAction(s)" +
                "Scenarios are triggered by ScenarioTrigger(s), for example a timer or a TCP incoming message.  "),
                @Tag(name = "RuleToArgument",description = "RuleToArgument Service")})
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
            RuleToArgumentHolderFilter filter,
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
            RuleToArgumentCreate creationContainer,
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
            RuleToArgumentUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        RuleToArgument flexiCoreRule=ruleUpdate.getId()!=null?service.getByIdOrNull(ruleUpdate.getId(),RuleToArgument.class,null,securityContext):null;
        if(flexiCoreRule==null ){
            throw new BadRequestException("No RuleToArgument with id "+ruleUpdate.getId());
        }
        service.validate(ruleUpdate, securityContext);
        return service.updateRulesToExecutionParameter(ruleUpdate, securityContext);
    }


}
