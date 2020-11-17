package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.JSFunctionParameter;
import com.flexicore.rules.request.JSFunctionParameterCreate;
import com.flexicore.rules.request.JSFunctionParameterFilter;
import com.flexicore.rules.request.JSFunctionParameterUpdate;
import com.flexicore.rules.service.JSFunctionParameterService;
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
@Path("plugins/JSFunctionParameter")
@Tag(name = "Rules")
@Tag(name = "JSFunctionParameter")
@Extension
@Component
public class JSFunctionParameterRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private JSFunctionParameterService service;

	@POST
	@Produces("application/json")
	@Path("/getAllJSFunctionParameters")
	@Operation(summary = "getAllJSFunctionParameters", description = "get all JSFunctionParameters, filtered, paged (optionally)")
	public PaginationResponse<JSFunctionParameter> getAllJSFunctionParameter(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid JSFunctionParameterFilter or an empty one (new instance, {}") JSFunctionParameterFilter filter,
			@Context SecurityContext securityContext) {
		service.validate(filter, securityContext);
		return service.getAllJSFunctionParameters(filter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createJSFunctionParameter")
	@Operation(summary = "createJSFunctionParameter", description = "create JSFunctionParameter instance")
	public JSFunctionParameter createJSFunctionParameter(
			@HeaderParam("authenticationKey") String authenticationKey,
			JSFunctionParameterCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		return service.createJSFunctionParameter(creationContainer, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateJSFunctionParameter")
	@Operation(summary = "updateJSFunctionParameter", description = "Update JSFunctionParameter")
	public JSFunctionParameter updateJSFunctionParameter(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid JSFunctionParameterUpdate instance with a valid Id of an existing JSFunctionParameter ") JSFunctionParameterUpdate jSFunctionParameterUpdate,
			@Context SecurityContext securityContext) {
		JSFunctionParameter jSFunctionParameter = jSFunctionParameterUpdate.getId() != null ? service.getByIdOrNull(jSFunctionParameterUpdate.getId(), JSFunctionParameter.class, null, securityContext) : null;
		if (jSFunctionParameter == null) {
			throw new BadRequestException("No JSFunctionParameter with id " + jSFunctionParameterUpdate.getId());
		}
		jSFunctionParameterUpdate.setJSFunctionParameter(jSFunctionParameter);
		service.validate(jSFunctionParameterUpdate, securityContext);
		return service.updateJSFunctionParameter(jSFunctionParameterUpdate,
				securityContext);
	}

}
