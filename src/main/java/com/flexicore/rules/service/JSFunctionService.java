package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.rules.model.JSFunction;
import com.flexicore.rules.repository.JSFunctionRepository;
import com.flexicore.rules.request.JSFunctionCreate;
import com.flexicore.rules.request.JSFunctionFilter;
import com.flexicore.rules.request.JSFunctionUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.util.List;
import java.util.Set;

@PluginInfo(version = 1)
@Extension
@Component
public class JSFunctionService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private JSFunctionRepository repository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public void validate(JSFunctionFilter jSFunctionArgumentFilter, SecurityContext securityContext) {
		baseclassNewService.validateFilter(jSFunctionArgumentFilter,securityContext);

	}

	
	public void validate(JSFunctionCreate creationContainer, SecurityContext securityContext) {
		baseclassNewService.validateCreate(creationContainer,securityContext);
		String evaluatingJSCodeId = creationContainer.getEvaluatingJSCodeId();
		FileResource fileResource = evaluatingJSCodeId != null ? getByIdOrNull(evaluatingJSCodeId, FileResource.class, null, securityContext) : null;
		if (fileResource == null) {
			throw new BadRequestException("No FileResource with id " + evaluatingJSCodeId);
		}
		creationContainer.setEvaluatingJSCode(fileResource);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public JSFunction createJSFunction(JSFunctionCreate creationContainer, SecurityContext securityContext) {
		JSFunction jSFunction = createJSFunctionNoMerge(
				creationContainer, securityContext);
		repository.merge(jSFunction);
		return jSFunction;
	}

	public JSFunction updateJSFunction(JSFunctionUpdate creationContainer, SecurityContext securityContext) {
		JSFunction jSFunction = creationContainer
				.getJSFunction();
		if (updateJSFunctionNoMerge(jSFunction, creationContainer)) {
			repository.merge(jSFunction);
		}
		return jSFunction;
	}

	
	public JSFunction createJSFunctionNoMerge(JSFunctionCreate creationContainer, SecurityContext securityContext) {
		JSFunction jSFunction = new JSFunction(creationContainer.getName(), securityContext);
		updateJSFunctionNoMerge(jSFunction, creationContainer);
		return jSFunction;
	}

	
	public boolean updateJSFunctionNoMerge(JSFunction jSFunction, JSFunctionCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer,jSFunction);
		if (creationContainer.getEvaluatingJSCode() != null && (jSFunction.getEvaluatingJSCode()==null||!creationContainer.getEvaluatingJSCode().getId().equals(jSFunction.getEvaluatingJSCode().getId()))) {
			jSFunction.setEvaluatingJSCode(creationContainer.getEvaluatingJSCode());
			update = true;
		}
		if (creationContainer.getMethodName() != null && !creationContainer.getMethodName().equals(jSFunction.getMethodName())) {
			jSFunction.setMethodName(creationContainer.getMethodName());
			update = true;
		}
		if (creationContainer.getReturnType() != null && !creationContainer.getReturnType().equals(jSFunction.getReturnType())) {
			jSFunction.setReturnType(creationContainer.getReturnType());
			update = true;
		}

		return update;
	}

	public PaginationResponse<JSFunction> getAllJSFunctions(JSFunctionFilter filter, SecurityContext securityContext) {
		List<JSFunction> list = repository.listAllJSFunctions(filter, securityContext);
		long count = repository.countAllJSFunctions(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public List<JSFunction> listAllJSFunctions(JSFunctionFilter filter, SecurityContext securityContext) {
		return repository.listAllJSFunctions(filter, securityContext);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}
}
