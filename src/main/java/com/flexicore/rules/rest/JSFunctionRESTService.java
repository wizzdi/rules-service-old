package com.flexicore.rules.rest;

import com.flexicore.annotations.OperationsInside;
import com.flexicore.annotations.ProtectedREST;
import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.RestServicePlugin;
import com.flexicore.rules.model.JSFunction;
import com.flexicore.rules.request.JSFunctionCreate;
import com.flexicore.rules.request.JSFunctionFilter;
import com.flexicore.rules.request.JSFunctionUpdate;
import com.flexicore.rules.service.JSFunctionService;
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
@Path("plugins/JSFunction")
@Tag(name = "Rules")
@Tag(name = "JSFunction")
@Extension
@Component
public class JSFunctionRESTService implements RestServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private JSFunctionService service;

	@POST
	@Produces("application/json")
	@Path("/getAllJSFunctions")
	@Operation(summary = "getAllJSFunctions", description = "get all JSFunctions, filtered, paged (optionally)")
	public PaginationResponse<JSFunction> getAllJSFunction(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid JSFunctionFilter or an empty one (new instance, {}") JSFunctionFilter filter,
			@Context SecurityContext securityContext) {
		service.validate(filter, securityContext);
		return service.getAllJSFunctions(filter, securityContext);
	}

	@POST
	@Produces("application/json")
	@Path("/createJSFunction")
	@Operation(summary = "createJSFunction", description = "create JSFunction instance")
	public JSFunction createJSFunction(
			@HeaderParam("authenticationKey") String authenticationKey,
			JSFunctionCreate creationContainer,
			@Context SecurityContext securityContext) {
		service.validate(creationContainer, securityContext);
		return service.createJSFunction(creationContainer, securityContext);
	}

	@PUT
	@Produces("application/json")
	@Path("/updateJSFunction")
	@Operation(summary = "updateJSFunction", description = "Update JSFunction")
	public JSFunction updateJSFunction(
			@HeaderParam("authenticationKey") String authenticationKey,
			@Parameter(description = "A valid JSFunctionUpdate instance with a valid Id of an existing JSFunction ") JSFunctionUpdate jSFunctionUpdate,
			@Context SecurityContext securityContext) {
		JSFunction jSFunction = jSFunctionUpdate.getId() != null ? service.getByIdOrNull(jSFunctionUpdate.getId(), JSFunction.class, null, securityContext) : null;
		if (jSFunction == null) {
			throw new BadRequestException("No JSFunction with id " + jSFunctionUpdate.getId());
		}
		jSFunctionUpdate.setJSFunction(jSFunction);
		service.validate(jSFunctionUpdate, securityContext);
		return service.updateJSFunction(jSFunctionUpdate,
				securityContext);
	}

}
