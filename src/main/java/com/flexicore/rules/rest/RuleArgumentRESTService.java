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
@Path("plugins/RuleArgument")
@Tag(name = "Rules")
//@Tag(name = "Rules", description = "Rules Service, the rules service provides required API to" +
//        "create scenarios containing hierarchical rules, the rules in a scenario describe an expression" +
//        "the expression consists of OR,AND,NOT and JavaScript snippet" +
//        "The Javascript is provided with arguments evaluated by the server in run time." +
//        "a Scenario evaluates to true or false, when true, it may fire ScenarioAction(s)" +
//        "Scenarios are triggered by ScenarioTrigger(s), for example a timer or a TCP incoming message.  ")
@Tag(name = "RuleArgument", description = "APIs for managing Rule arguments " +
        "RuleArgument inherits from Dynamic execution and is designed to bring at evaluation time a list of objects defined by the RuleArgument" +
        "The returned objects can be a list of instances , a count, a boolean etc." +
        "The list is returned by a Method of a Dynamic invoker which can be best described as server side reflection." +
        "The RuleArgument is used by a Rule that has a valid JavaScript or by a simple AND/OR/NOT Rule." +
        "The main idea behind RuleArgument is to use filtering based on requested class to retrieve at runtime the correct dataset the Javascript based Rule needs. Multiple such RuleArguments are " +
        "available to a Script." +
        "Adding more available types of data-sets is a server side only task, new server side plug-ins can add more invokers and more methods compatible with the Rules system. ")

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
            @RequestBody(description = "test parameter") RuleArgumentFilter filter,
            @Context SecurityContext securityContext) {
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

            @Parameter(description = "Properly created RuleArgumentCreate") RuleArgumentCreate creationContainer,
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
            @Parameter(description = "Properly set RuleArgumentUpdate instance")  RuleArgumentUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRuleArgument flexiCoreRuleArgument = ruleUpdate.getId() != null ? service.getByIdOrNull(ruleUpdate.getId(), FlexiCoreRuleArgument.class, null, securityContext) : null;
        if (flexiCoreRuleArgument == null) {
            throw new BadRequestException("No Rule with id " + ruleUpdate.getId());
        }
        service.validate(ruleUpdate, securityContext);
        return service.updateRuleArgument(ruleUpdate, securityContext);
    }


}
