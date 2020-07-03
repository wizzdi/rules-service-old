package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.DataSource;
import com.flexicore.rules.model.ScenarioToDataSource;
import com.flexicore.rules.repository.ScenarioToDataSourceRepository;
import com.flexicore.rules.request.ScenarioToDataSourceCreate;
import com.flexicore.rules.request.ScenarioToDataSourceFilter;
import com.flexicore.rules.request.ScenarioToDataSourceUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioToDataSourceService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioToDataSourceRepository repository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	public <T extends Baseclass> List<T> listByIds(Class<T> c, Set<String> ids,
			SecurityContext securityContext) {
		return repository.listByIds(c, ids, securityContext);
	}

	public void validate(ScenarioToDataSourceFilter scenarioToDataSourceFilter,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(scenarioToDataSourceFilter,securityContext);

		Set<String> actionsIds = scenarioToDataSourceFilter.getActionsIds();
		Map<String, DataSource> actionMap = actionsIds.isEmpty()
				? new HashMap<>()
				: listByIds(DataSource.class, actionsIds, securityContext)
						.parallelStream().collect(
								Collectors.toMap(f -> f.getId(), f -> f));
		actionsIds.removeAll(actionMap.keySet());
		if (!actionsIds.isEmpty()) {
			throw new BadRequestException("No Scenario Action with ids "
					+ actionsIds);
		}
		scenarioToDataSourceFilter.setDataSources(new ArrayList<>(actionMap
				.values()));

		Set<String> scenarioIds = scenarioToDataSourceFilter.getScenarioIds();
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
		scenarioToDataSourceFilter.setScenarios(new ArrayList<>(scenarioMap
				.values()));
	}

	public void validate(ScenarioToDataSourceCreate creationContainer,
			SecurityContext securityContext) {
		baseclassNewService.validateCreate(creationContainer,securityContext);
		String scenarioId = creationContainer.getScenarioId();
		Scenario scenario = scenarioId != null ? getByIdOrNull(scenarioId,
				Scenario.class, null, securityContext) : null;
		if (scenario == null && scenarioId != null) {
			throw new BadRequestException("No Scenario with id " + scenarioId);
		}
		creationContainer.setScenario(scenario);

		String actionId = creationContainer.getActionId();
		DataSource action = actionId != null ? getByIdOrNull(actionId,
				DataSource.class, null, securityContext) : null;
		if (action == null && actionId != null) {
			throw new BadRequestException("No DataSource with id "
					+ actionId);
		}
		creationContainer.setDataSource(action);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public ScenarioToDataSource createScenarioToDataSource(
			ScenarioToDataSourceCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioToDataSource scenarioToDataSource = createScenarioToDataSourceNoMerge(
				creationContainer, securityContext);
		repository.merge(scenarioToDataSource);
		return scenarioToDataSource;

	}

	public ScenarioToDataSource updateScenarioToDataSource(
			ScenarioToDataSourceUpdate creationContainer,
			SecurityContext securityContext) {
		ScenarioToDataSource scenarioToDataSource = creationContainer
				.getScenarioToDataSource();
		if (updateScenarioToDataSourceNoMerge(scenarioToDataSource, creationContainer)) {
			repository.merge(scenarioToDataSource);

		}
		return scenarioToDataSource;

	}

	private ScenarioToDataSource createScenarioToDataSourceNoMerge(
			ScenarioToDataSourceCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioToDataSource scenarioToDataSource = new ScenarioToDataSource(creationContainer.getName(), securityContext);
		updateScenarioToDataSourceNoMerge(scenarioToDataSource, creationContainer);
		return scenarioToDataSource;
	}

	private boolean updateScenarioToDataSourceNoMerge(ScenarioToDataSource scenarioToDataSource, ScenarioToDataSourceCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer,scenarioToDataSource);
		if (creationContainer.getScenario() != null && (scenarioToDataSource.getScenario() == null || !creationContainer.getScenario().getId().equals(scenarioToDataSource.getScenario().getId()))) {
			scenarioToDataSource.setScenario(creationContainer.getScenario());
			update = true;
		}

		if (creationContainer.getDataSource() != null && (scenarioToDataSource.getDataSource() == null || !creationContainer.getDataSource().getId().equals(scenarioToDataSource.getDataSource().getId()))) {
			scenarioToDataSource.setDataSource(creationContainer
					.getDataSource());
			update = true;
		}

		if (creationContainer.getEnabled() != null && creationContainer.getEnabled() != scenarioToDataSource.isEnabled()) {
			scenarioToDataSource.setEnabled(creationContainer.getEnabled());
			update = true;
		}

		if (creationContainer.getOrdinal() != null && creationContainer.getOrdinal() != scenarioToDataSource.getOrdinal()) {
			scenarioToDataSource.setOrdinal(creationContainer.getOrdinal());
			update = true;
		}
		return update;

	}

	public PaginationResponse<ScenarioToDataSource> getAllScenarioToDataSources(ScenarioToDataSourceFilter filter, SecurityContext securityContext) {
		List<ScenarioToDataSource> list = listAllScenarioToDataSource(filter, securityContext);
		long count = repository.countAllScenarioToDataSources(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public List<ScenarioToDataSource> listAllScenarioToDataSource(ScenarioToDataSourceFilter filter, SecurityContext securityContext) {
		return repository.listAllScenarioToDataSources(filter, securityContext);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}
}
