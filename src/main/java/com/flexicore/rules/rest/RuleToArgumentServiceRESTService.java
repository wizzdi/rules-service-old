package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;

import com.flexicore.annotations.ProtectedREST;
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

import javax.interceptor.Interceptors;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/RuleToArgument")
@Tag(name = "Rules")
@Extension
@Component
public class RuleToArgumentServiceRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
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
		return service
				.getAllRulesToExecutionParameters(filter, securityContext);
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
		return service.createRulesToExecutionParameter(creationContainer,
				securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateRuleToArgument")
	@Operation(summary = "updateRuleToArgument", description = "Update RuleToArgument")
	public RuleToArgument updateRuleToArgument(
			@HeaderParam("authenticationKey") String authenticationKey,
			RuleToArgumentUpdate ruleUpdate,
			@Context SecurityContext securityContext) {
		RuleToArgument ruleToArgument = ruleUpdate.getId() != null ? service
				.getByIdOrNull(ruleUpdate.getId(), RuleToArgument.class, null,
						securityContext) : null;
		if (ruleToArgument == null) {
			throw new BadRequestException("No RuleToArgument with id "
					+ ruleUpdate.getId());
		}
		ruleUpdate.setRuleToArgument(ruleToArgument);
		service.validate(ruleUpdate, securityContext);
		return service.updateRulesToExecutionParameter(ruleUpdate,
				securityContext);
	}

}
