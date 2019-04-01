package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.request.EvaluateRuleRequest;
import com.flexicore.rules.request.RuleCreate;
import com.flexicore.rules.request.RuleUpdate;
import com.flexicore.rules.request.RulesFilter;
import com.flexicore.rules.response.EvaluateRuleResponse;
import com.flexicore.rules.service.RulesService;
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
@Path("plugins/Rules")
@Tag(name="Rules")
@OpenAPIDefinition(
        tags = {@Tag(name = "Rules",description = "Rules Service"),
                @Tag(name = "RulesToExecutionParameter",description = "RulesToExecutionParameter Service")})
public class RulesRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private RulesService service;

    @POST
    @Produces("application/json")
    @Path("/createRule")
    @Operation(summary = "getAllRules", description = "create Rule")
    public PaginationResponse<FlexiCoreRule> getAllRules(
            @HeaderParam("authenticationKey") String authenticationKey,
            RulesFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllRules(filter, securityContext);
    }

    @POST
    @Produces("application/json")
    @Path("/evaluateRule")
    @Operation(summary = "evaluateRule", description = "Evaluate Rule")
    public EvaluateRuleResponse evaluateRule(
            @HeaderParam("authenticationKey") String authenticationKey,
            EvaluateRuleRequest evaluateRuleRequest,
            @Context SecurityContext securityContext) {
        service.validate(evaluateRuleRequest, securityContext);
        return service.evaluateRule(evaluateRuleRequest, securityContext);
    }

    @POST
    @Produces("application/json")
    @Path("/createRule")
    @Operation(summary = "createRule", description = "create Rule")
    public FlexiCoreRule createRule(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createRule(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateRule")
    @Operation(summary = "updateRule", description = "Update Rule")
    public FlexiCoreRule updateRule(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRule flexiCoreRule=ruleUpdate.getId()!=null?service.getByIdOrNull(ruleUpdate.getId(),FlexiCoreRule.class,null,securityContext):null;
        if(flexiCoreRule==null ){
            throw new BadRequestException("No Rule with id "+ruleUpdate.getId());
        }
        service.validate(ruleUpdate, securityContext);
        return service.updateRule(ruleUpdate, securityContext);
    }


}
