package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interceptors.DynamicResourceInjector;
import com.flexicore.interceptors.SecurityImposer;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleLink;
import com.flexicore.rules.model.FlexiCoreRuleOp;
import com.flexicore.rules.request.*;
import com.flexicore.rules.response.EvaluateRuleResponse;
import com.flexicore.rules.service.RulesService;
import com.flexicore.security.SecurityContext;
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
@Path("plugins/Rules")
@Tag(name = "Rules", description = "Rules Service, the rules service provides required API to " +
        "create scenarios containing hierarchical rules, the rules in a scenario describe an expression " +
        "the expression consists of OR,AND,NOT and JavaScript snippet " +
        "The Javascript is provided with arguments evaluated by the server in run time. " +
        "a Scenario evaluates to true or false, when true, it may fire ScenarioAction(s) " +
        "Scenarios are triggered by ScenarioTrigger(s), for example a timer or a TCP incoming message.  "
)


public class RulesRESTService implements RestServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private RulesService service;

    @POST
    @Produces("application/json")
    @Path("/getAllRules")
    @Operation(summary = "getAllRules", description = "get all rules , these are filtered by the server to include only instances the current user can view")
    public PaginationResponse<FlexiCoreRule> getAllRules(
            @HeaderParam("authenticationKey") String authenticationKey,
            @RequestBody(description = "Valid RulesFilter, can be empty, avoid using non paginated responses", required = true) RulesFilter filter,
            @Context SecurityContext securityContext) {
        service.validate(filter, securityContext);
        return service.getAllRules(filter, securityContext);
    }

    @POST
    @Produces("application/json")
    @Path("/evaluateRule")
    @Operation(summary = "evaluateRule", description = "Evaluate Rule, use for testing a single rule.")
    public EvaluateRuleResponse evaluateRule(
            @HeaderParam("authenticationKey") String authenticationKey,
            @RequestBody(description = "Includes RuleId", required = true) EvaluateRuleRequest evaluateRuleRequest,
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
            @RequestBody(description = "Valid RuleCreate container", required = true) RuleCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createRule(creationContainer, securityContext);
    }


    @POST
    @Produces("application/json")
    @Path("/getAllRuleLinks")
    @Operation(summary = "getAllRuleLinks", description = "getAllRuleLinks, these are child Rules of this Rule, defines the hierarchic nature of the Rules system")
    public PaginationResponse<FlexiCoreRuleLink> getAllRuleLinks(
            @HeaderParam("authenticationKey") String authenticationKey,
            @RequestBody(description = "Valid RuleLinkFilter", required = true) RuleLinkFilter ruleLinkFilter,
            @Context SecurityContext securityContext) {
        service.validate(ruleLinkFilter, securityContext);
        return service.getAllRuleLinks(ruleLinkFilter, securityContext);
    }


    @POST
    @Produces("application/json")
    @Path("/createRuleLink")
    @Operation(summary = "createRuleLink", description = "create Rule link")
    public FlexiCoreRuleLink createRuleLink(
            @HeaderParam("authenticationKey") String authenticationKey,
            @RequestBody(description = "Valid RuleLinkCreate container ") RuleLinkCreate creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createRuleLink(creationContainer, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateRuleLink")
    @Operation(summary = "updateRuleLink", description = "update Rule link")
    public FlexiCoreRuleLink updateRuleLink(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleLinkUpdate ruleLinkUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRuleLink flexiCoreRuleLink = ruleLinkUpdate.getId() != null ? service.getByIdOrNull(ruleLinkUpdate.getId(), FlexiCoreRuleLink.class, null, securityContext) : null;
        if (flexiCoreRuleLink == null) {
            throw new BadRequestException("No FlexiCoreRuleLink with id " + ruleLinkUpdate.getId());
        }
        ruleLinkUpdate.setFlexiCoreRuleLink(flexiCoreRuleLink);
        service.validate(ruleLinkUpdate, securityContext);
        return service.updateRuleLink(ruleLinkUpdate, securityContext);
    }

    @POST
    @Produces("application/json")
    @Path("/createRuleOp")
    @Operation(summary = "createRuleOp", description = "create Rule op")
    public FlexiCoreRuleOp createRuleOp(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleCreateOp creationContainer,
            @Context SecurityContext securityContext) {
        service.validate(creationContainer, securityContext);
        return service.createRuleOp(creationContainer, securityContext);
    }


    @PUT
    @Produces("application/json")
    @Path("/updateRuleOp")
    @Operation(summary = "updateRuleOp", description = "update Rule op")
    public FlexiCoreRuleOp updateRuleOp(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleUpdateOp ruleUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRuleOp flexiCoreRuleOp = ruleUpdate.getId() != null ? service.getByIdOrNull(ruleUpdate.getId(), FlexiCoreRuleOp.class, null, securityContext) : null;
        if (flexiCoreRuleOp == null) {
            throw new BadRequestException("No FlexiCoreRuleOp with id " + ruleUpdate.getId());
        }
        ruleUpdate.setFlexiCoreRuleOp(flexiCoreRuleOp);
        return service.updateRuleOp(ruleUpdate, securityContext);
    }

    @PUT
    @Produces("application/json")
    @Path("/updateRule")
    @Operation(summary = "updateRule", description = "Update Rule")
    public FlexiCoreRule updateRule(
            @HeaderParam("authenticationKey") String authenticationKey,
            RuleUpdate ruleUpdate,
            @Context SecurityContext securityContext) {
        FlexiCoreRule flexiCoreRule = ruleUpdate.getId() != null ? service.getByIdOrNull(ruleUpdate.getId(), FlexiCoreRule.class, null, securityContext) : null;
        if (flexiCoreRule == null) {
            throw new BadRequestException("No Rule with id " + ruleUpdate.getId());
        }
        ruleUpdate.setFlexiCoreRule(flexiCoreRule);
        service.validate(ruleUpdate, securityContext);
        return service.updateRule(ruleUpdate, securityContext);
    }


}
