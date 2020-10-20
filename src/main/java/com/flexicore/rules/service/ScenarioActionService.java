package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.rules.model.ScenarioAction;
import com.flexicore.rules.repository.ScenarioActionRepository;
import com.flexicore.rules.request.ScenarioActionCreate;
import com.flexicore.rules.request.ScenarioActionFilter;
import com.flexicore.rules.request.ScenarioActionUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.ws.rs.BadRequestException;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioActionService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioActionRepository repository;

	@Autowired
	private DynamicInvokersService dynamicInvokersService;

	public void validate(ScenarioActionFilter scenarioActionArgumentFilter,
			SecurityContext securityContext) {

	}

	public void validate(ScenarioActionCreate creationContainer,
			SecurityContext securityContext) {
		String dynamicExecutionId = creationContainer.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId != null
				? getByIdOrNull(dynamicExecutionId, DynamicExecution.class,
						null, securityContext) : null;
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new BadRequestException("No Dynamic Execution With id "
					+ dynamicExecutionId);
		}
		creationContainer.setDynamicExecution(dynamicExecution);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public ScenarioAction createScenarioAction(
			ScenarioActionCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioAction scenarioAction = createScenarioActionNoMerge(
				creationContainer, securityContext);
		repository.merge(scenarioAction);
		return scenarioAction;

	}

	public ScenarioAction updateScenarioAction(
			ScenarioActionUpdate creationContainer,
			SecurityContext securityContext) {
		ScenarioAction scenarioAction = creationContainer.getScenarioAction();
		if (updateScenarioActionNoMerge(scenarioAction, creationContainer)) {
			repository.merge(scenarioAction);

		}
		return scenarioAction;

	}

	private ScenarioAction createScenarioActionNoMerge(
			ScenarioActionCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioAction scenarioAction = new ScenarioAction(
				creationContainer.getName(), securityContext);
		updateScenarioActionNoMerge(scenarioAction, creationContainer);
		return scenarioAction;
	}

	private boolean updateScenarioActionNoMerge(ScenarioAction scenarioAction,
			ScenarioActionCreate scenarioActionCreate) {
		boolean update = false;
		if (scenarioActionCreate.getName() != null
				&& !scenarioActionCreate.getName().equals(
						scenarioAction.getName())) {
			scenarioAction.setName(scenarioActionCreate.getName());
			update = true;
		}
		if (scenarioActionCreate.getDescription() != null
				&& !scenarioActionCreate.getDescription().equals(
						scenarioAction.getDescription())) {
			scenarioAction
					.setDescription(scenarioActionCreate.getDescription());
			update = true;
		}
		if (scenarioActionCreate.getDynamicExecution() != null
				&& (scenarioAction.getDynamicExecution() == null || !scenarioActionCreate
						.getDynamicExecution().getId()
						.equals(scenarioAction.getDynamicExecution().getId()))) {
			scenarioAction.setDynamicExecution(scenarioActionCreate
					.getDynamicExecution());
			update = true;
		}
		return update;

	}

	public PaginationResponse<ScenarioAction> getAllScenarioActions(
			ScenarioActionFilter filter, SecurityContext securityContext) {
		List<ScenarioAction> list = repository.listAllScenarioActions(filter,
				securityContext);
		long count = repository
				.countAllScenarioActions(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}
}
