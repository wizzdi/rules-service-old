package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.rules.interfaces.IScenarioTriggerService;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.repository.ScenarioTriggerRepository;
import com.flexicore.rules.request.ScenarioTriggerCreate;
import com.flexicore.rules.request.ScenarioTriggerEvent;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioTriggerService implements IScenarioTriggerService {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioTriggerRepository repository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public void validate(ScenarioTriggerFilter scenarioTriggerArgumentFilter, SecurityContext securityContext) {
		baseclassNewService.validateFilter(scenarioTriggerArgumentFilter,securityContext);

	}

	@Override
	public void validate(ScenarioTriggerCreate creationContainer, SecurityContext securityContext) {
		baseclassNewService.validateCreate(creationContainer,securityContext);
		String evaluatingJSCodeId = creationContainer.getEvaluatingJSCodeId();
		FileResource fileResource = evaluatingJSCodeId != null ? getByIdOrNull(evaluatingJSCodeId, FileResource.class, null, securityContext) : null;
		if (fileResource == null) {
			throw new BadRequestException("No Scenario with id " + evaluatingJSCodeId);
		}
		creationContainer.setEvaluatingJSCode(fileResource);
	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public ScenarioTrigger createScenarioTrigger(ScenarioTriggerCreate creationContainer, SecurityContext securityContext) {
		ScenarioTrigger scenarioTrigger = createScenarioTriggerNoMerge(
				creationContainer, securityContext);
		repository.merge(scenarioTrigger);
		return scenarioTrigger;
	}

	public ScenarioTrigger updateScenarioTrigger(ScenarioTriggerUpdate creationContainer, SecurityContext securityContext) {
		ScenarioTrigger scenarioTrigger = creationContainer
				.getScenarioTrigger();
		if (updateScenarioTriggerNoMerge(scenarioTrigger, creationContainer)) {
			repository.merge(scenarioTrigger);
		}
		return scenarioTrigger;
	}

	@Override
	public ScenarioTrigger createScenarioTriggerNoMerge(ScenarioTriggerCreate creationContainer, SecurityContext securityContext) {
		ScenarioTrigger scenarioTrigger = new ScenarioTrigger(creationContainer.getName(), securityContext);
		updateScenarioTriggerNoMerge(scenarioTrigger, creationContainer);
		return scenarioTrigger;
	}

	@Override
	public boolean updateScenarioTriggerNoMerge(ScenarioTrigger scenarioTrigger, ScenarioTriggerCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer,scenarioTrigger);
		if (creationContainer.getScenarioTriggerType() != null && (creationContainer.getScenarioTriggerType()==null||!creationContainer.getScenarioTriggerType().getId().equals(scenarioTrigger.getScenarioTriggerType().getId()))) {
			scenarioTrigger.setScenarioTriggerType(creationContainer.getScenarioTriggerType());
			update = true;
		}
		if (creationContainer.getValidFrom() != null && !creationContainer.getValidFrom().equals(scenarioTrigger.getValidFrom())) {
			scenarioTrigger.setValidFrom(creationContainer.getValidFrom());
			update = true;
		}
		if (creationContainer.getValidTill() != null && !creationContainer.getValidTill().equals(scenarioTrigger.getValidTill())) {
			scenarioTrigger.setValidTill(creationContainer.getValidTill());
			update = true;
		}

		if (creationContainer.getActiveTill() != null && !creationContainer.getActiveTill().equals(scenarioTrigger.getActiveTill())) {
			scenarioTrigger.setActiveTill(creationContainer.getActiveTill());
			update = true;
		}
		if (creationContainer.getEvaluatingJSCode() != null && (creationContainer.getEvaluatingJSCode()==null||!creationContainer.getEvaluatingJSCode().getId().equals(scenarioTrigger.getEvaluatingJSCode().getId()))) {
			scenarioTrigger.setEvaluatingJSCode(creationContainer.getEvaluatingJSCode());
			update = true;
		}
		return update;
	}

	public PaginationResponse<ScenarioTrigger> getAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
		List<ScenarioTrigger> list = repository.listAllScenarioTriggers(filter, securityContext);
		long count = repository.countAllScenarioTriggers(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}


	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids, SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public List<ScenarioTrigger> listAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
		return repository.listAllScenarioTriggers(filter, securityContext);
	}
}
