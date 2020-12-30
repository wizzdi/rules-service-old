package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.JSFunction;
import com.flexicore.rules.model.JSFunctionParameter;
import com.flexicore.rules.repository.JSFunctionParameterRepository;
import com.flexicore.rules.request.JSFunctionParameterCreate;
import com.flexicore.rules.request.JSFunctionParameterFilter;
import com.flexicore.rules.request.JSFunctionParameterUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class JSFunctionParameterService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private JSFunctionParameterRepository repository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public void validate(JSFunctionParameterFilter jSFunctionParameterArgumentFilter, SecurityContext securityContext) {
		baseclassNewService.validateFilter(jSFunctionParameterArgumentFilter,securityContext);
		Set<String> jsFunctionsIds=jSFunctionParameterArgumentFilter.getJsFunctionsIds();
		Map<String,JSFunction> jsFunctionMap=jsFunctionsIds.isEmpty()?new HashMap<>():listByIds(JSFunction.class,jsFunctionsIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
		jsFunctionsIds.removeAll(jsFunctionMap.keySet());
		if(!jsFunctionsIds.isEmpty()){
			throw new BadRequestException("No JSFunctions with ids "+jsFunctionsIds);
		}
		jSFunctionParameterArgumentFilter.setJsFunctions(new ArrayList<>(jsFunctionMap.values()));

	}

	
	public void validate(JSFunctionParameterCreate creationContainer, SecurityContext securityContext) {
		baseclassNewService.validateCreate(creationContainer,securityContext);
		String evaluatingJSCodeId = creationContainer.getJsFunctionId();
		JSFunction fileResource = evaluatingJSCodeId != null ? getByIdOrNull(evaluatingJSCodeId, JSFunction.class, null, securityContext) : null;
		if (fileResource == null) {
			throw new BadRequestException("No JSFunction with id " + evaluatingJSCodeId);
		}
		creationContainer.setJsFunction(fileResource);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public JSFunctionParameter createJSFunctionParameter(JSFunctionParameterCreate creationContainer, SecurityContext securityContext) {
		JSFunctionParameter jSFunctionParameter = createJSFunctionParameterNoMerge(
				creationContainer, securityContext);
		repository.merge(jSFunctionParameter);
		return jSFunctionParameter;
	}

	public JSFunctionParameter updateJSFunctionParameter(JSFunctionParameterUpdate creationContainer, SecurityContext securityContext) {
		JSFunctionParameter jSFunctionParameter = creationContainer
				.getJSFunctionParameter();
		if (updateJSFunctionParameterNoMerge(jSFunctionParameter, creationContainer)) {
			repository.merge(jSFunctionParameter);
		}
		return jSFunctionParameter;
	}

	
	public JSFunctionParameter createJSFunctionParameterNoMerge(JSFunctionParameterCreate creationContainer, SecurityContext securityContext) {
		JSFunctionParameter jSFunctionParameter = new JSFunctionParameter(creationContainer.getName(), securityContext);
		updateJSFunctionParameterNoMerge(jSFunctionParameter, creationContainer);
		return jSFunctionParameter;
	}

	
	public boolean updateJSFunctionParameterNoMerge(JSFunctionParameter jSFunctionParameter, JSFunctionParameterCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer,jSFunctionParameter);
		if (creationContainer.getJsFunction() != null && (jSFunctionParameter.getJsFunction()==null||!creationContainer.getJsFunction().getId().equals(jSFunctionParameter.getJsFunction().getId()))) {
			jSFunctionParameter.setJsFunction(creationContainer.getJsFunction());
			update = true;
		}
		if (creationContainer.getOrdinal() != null && !creationContainer.getOrdinal().equals(jSFunctionParameter.getOrdinal())) {
			jSFunctionParameter.setOrdinal(creationContainer.getOrdinal());
			update = true;
		}
		if (creationContainer.getParameterType() != null && !creationContainer.getParameterType().equals(jSFunctionParameter.getParameterType())) {
			jSFunctionParameter.setParameterType(creationContainer.getParameterType());
			update = true;
		}

		return update;
	}

	public PaginationResponse<JSFunctionParameter> getAllJSFunctionParameters(JSFunctionParameterFilter filter, SecurityContext securityContext) {
		List<JSFunctionParameter> list = repository.listAllJSFunctionParameters(filter, securityContext);
		long count = repository.countAllJSFunctionParameters(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public List<JSFunctionParameter> listAllJSFunctionParameters(JSFunctionParameterFilter filter, SecurityContext securityContext) {
		return repository.listAllJSFunctionParameters(filter, securityContext);
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public void massMerge(List<?> toMerge) {
		repository.massMerge(toMerge);
	}
}
