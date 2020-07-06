package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.ActionReplacement;
import com.flexicore.rules.request.ActionReplacementCreate;
import com.flexicore.rules.request.ActionReplacementFilter;
import com.flexicore.rules.request.ActionReplacementUpdate;
import com.flexicore.rules.service.ActionReplacementService;
import com.flexicore.security.SecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@PluginInfo(version = 1)
@OperationsInside
@ProtectedREST
@Path("plugins/ActionReplacement")
@Tag(name = "Rules")
@Tag(name = "Scenarios")
@Tag(name = "Triggers")
@Extension
@Component
public class ActionReplacementRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ActionReplacementService service;

	@POST
	@Produces("application/json")
	@Path("/getAllActionReplacements")
	@Operation(summary = "getAllActionReplacements", description = "get all ActionReplacements, filtered, paged (optionally)")
	public PaginationResponse<ActionReplacement> getAllActionReplacement(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid ActionReplacementFilter or an empty one (new instance, {}") ActionReplacementFilter filter,
			@Context SecurityContext securityContext) {
		service.validate(filter, securityContext);
		return service.getAllActionReplacements(filter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createActionReplacement")
	@Operation(summary = "createActionReplacement", description = "create ActionReplacement instance")
	public ActionReplacement createActionReplacement(
			@HeaderParam("authenticationKey") String authenticationKey,
			ActionReplacementCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		return service.createActionReplacement(creationContainer, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateActionReplacement")
	@Operation(summary = "updateActionReplacement", description = "Update ActionReplacement")
	public ActionReplacement updateActionReplacement(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid ActionReplacementUpdate instance with a valid Id of an existing ActionReplacement ") ActionReplacementUpdate actionReplacementUpdate,
			@Context SecurityContext securityContext) {
		ActionReplacement actionReplacement = actionReplacementUpdate.getId() != null ? service.getByIdOrNull(actionReplacementUpdate.getId(), ActionReplacement.class, null, securityContext) : null;
		if (actionReplacement == null) {
			throw new BadRequestException("No ActionReplacement with id " + actionReplacementUpdate.getId());
		}
		actionReplacementUpdate.setActionReplacement(actionReplacement);
		service.validate(actionReplacementUpdate, securityContext);
		return service.updateActionReplacement(actionReplacementUpdate,
				securityContext);
	}

}
