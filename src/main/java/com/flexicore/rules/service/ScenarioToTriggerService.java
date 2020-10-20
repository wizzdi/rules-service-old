package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.*;
import com.flexicore.rules.repository.ScenarioToTriggerRepository;
import com.flexicore.rules.request.ScenarioToTriggerCreate;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.rules.request.ScenarioToTriggerUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioToTriggerService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioToTriggerRepository repository;

	@Autowired
	private DynamicInvokersService dynamicInvokersService;

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public void validate(
			ScenarioToTriggerFilter scenarioToTriggerArgumentFilter,
			SecurityContext securityContext) {

		Set<String> scenarioTriggerIds = scenarioToTriggerArgumentFilter
				.getScenarioTriggerIds();
		Map<String, ScenarioTrigger> scenarioTriggerMap = scenarioTriggerIds
				.isEmpty() ? new HashMap<>() : listByIds(ScenarioTrigger.class,
				scenarioTriggerIds, securityContext).parallelStream().collect(
				Collectors.toMap(f -> f.getId(), f -> f));
		scenarioTriggerIds.removeAll(scenarioTriggerMap.keySet());
		if (!scenarioTriggerIds.isEmpty()) {
			throw new BadRequestException("No Scenario Triggers with ids "
					+ scenarioTriggerIds);
		}
		scenarioToTriggerArgumentFilter.setScenarioTriggers(new ArrayList<>(
				scenarioTriggerMap.values()));

		Set<String> scenarioIds = scenarioToTriggerArgumentFilter
				.getScenarioIds();
		Map<String, Scenario> scenarioMap = scenarioIds.isEmpty()
				? new HashMap<>()
				: listByIds(Scenario.class, scenarioIds, securityContext)
						.parallelStream().collect(
								Collectors.toMap(f -> f.getId(), f -> f));
		scenarioIds.removeAll(scenarioMap.keySet());
		if (!scenarioIds.isEmpty()) {
			throw new BadRequestException("No Scenarios with ids "
					+ scenarioIds);
		}
		scenarioToTriggerArgumentFilter.setScenarios(new ArrayList<>(
				scenarioMap.values()));
	}

	public void validate(ScenarioToTriggerCreate creationContainer,
			SecurityContext securityContext) {
		String scenarioId = creationContainer.getScenarioId();
		Scenario scenario = scenarioId != null ? getByIdOrNull(scenarioId,
				Scenario.class, null, securityContext) : null;
		if (scenario == null && scenarioId != null) {
			throw new BadRequestException("No Scenario with id " + scenarioId);
		}
		creationContainer.setScenario(scenario);

		String triggerId = creationContainer.getTriggerId();
		ScenarioTrigger trigger = triggerId != null ? getByIdOrNull(triggerId,
				ScenarioTrigger.class, null, securityContext) : null;
		if (trigger == null && triggerId != null) {
			throw new BadRequestException("No ScenarioTrigger with id "
					+ triggerId);
		}
		creationContainer.setScenarioTrigger(trigger);

		String triggerManagerId = creationContainer.getTriggerManagerId();
		TriggerManager triggerManager = triggerManagerId != null
				? getByIdOrNull(triggerManagerId, TriggerManager.class, null,
						securityContext) : null;
		if (triggerManager == null && triggerManagerId != null) {
			throw new BadRequestException("No TriggerManager with id "
					+ triggerManagerId);
		}
		creationContainer.setTriggerManager(triggerManager);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public ScenarioToTrigger createScenarioToTrigger(
			ScenarioToTriggerCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioToTrigger scenarioToTrigger = createScenarioToTriggerNoMerge(
				creationContainer, securityContext);
		repository.merge(scenarioToTrigger);
		return scenarioToTrigger;

	}

	public ScenarioToTrigger updateScenarioToTrigger(
			ScenarioToTriggerUpdate creationContainer,
			SecurityContext securityContext) {
		ScenarioToTrigger scenarioToTrigger = creationContainer
				.getScenarioToTrigger();
		if (updateScenarioToTriggerNoMerge(scenarioToTrigger, creationContainer)) {
			repository.merge(scenarioToTrigger);

		}
		return scenarioToTrigger;

	}

	private ScenarioToTrigger createScenarioToTriggerNoMerge(
			ScenarioToTriggerCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioToTrigger scenarioToTrigger = new ScenarioToTrigger(creationContainer.getName(), securityContext);
		updateScenarioToTriggerNoMerge(scenarioToTrigger, creationContainer);
		return scenarioToTrigger;
	}

	private boolean updateScenarioToTriggerNoMerge(
			ScenarioToTrigger scenarioToTrigger,
			ScenarioToTriggerCreate creationContainer) {
		boolean update = false;
		if (creationContainer.getName() != null
				&& !creationContainer.getName().equals(
						scenarioToTrigger.getName())) {
			scenarioToTrigger.setName(creationContainer.getName());
			update = true;
		}

		if (creationContainer.getDescription() != null
				&& !creationContainer.getDescription().equals(
						scenarioToTrigger.getDescription())) {
			scenarioToTrigger
					.setDescription(creationContainer.getDescription());
			update = true;
		}
		if (creationContainer.getScenario() != null
				&& (scenarioToTrigger.getScenario() == null || !creationContainer
						.getScenario().getId()
						.equals(scenarioToTrigger.getScenario().getId()))) {
			scenarioToTrigger.setScenario(creationContainer.getScenario());
			update = true;
		}

		if (creationContainer.getScenarioTrigger() != null
				&& (scenarioToTrigger.getScenarioTrigger() == null || !creationContainer
						.getScenarioTrigger().getId()
						.equals(scenarioToTrigger.getScenarioTrigger().getId()))) {
			scenarioToTrigger.setScenarioTrigger(creationContainer
					.getScenarioTrigger());
			update = true;
		}

		if (creationContainer.getEnabled() != null
				&& creationContainer.getEnabled() != scenarioToTrigger
						.isEnabled()) {
			scenarioToTrigger.setEnabled(creationContainer.getEnabled());
			update = true;
		}
		if (creationContainer.getTriggerManager() != null
				&& (scenarioToTrigger.getTriggerManager() == null || !creationContainer
						.getTriggerManager().getId()
						.equals(scenarioToTrigger.getTriggerManager().getId()))) {
			scenarioToTrigger.setTriggerManager(creationContainer
					.getTriggerManager());
			update = true;
		}

		return update;

	}

	public PaginationResponse<ScenarioToTrigger> getAllScenarioToTriggers(
			ScenarioToTriggerFilter filter, SecurityContext securityContext) {
		List<ScenarioToTrigger> list = listAllScenarioToTrigger(filter,
				securityContext);
		long count = repository.countAllScenarioToTriggers(filter,
				securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public List<ScenarioToTrigger> listAllScenarioToTrigger(
			ScenarioToTriggerFilter filter, SecurityContext securityContext) {
		return repository.listAllScenarioToTriggers(filter, securityContext);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}
}
