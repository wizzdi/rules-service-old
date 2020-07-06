package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.repository.ScenarioRepository;
import com.flexicore.rules.request.ClearLogRequest;
import com.flexicore.rules.request.ScenarioCreate;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.ScenarioUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.service.FileResourceService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioRepository repository;

	@Autowired
	private FileResourceService fileResourceService;

	@Autowired
	private BaseclassNewService baseclassNewService;

	@Autowired
	private Logger logger;

	public void validate(ScenarioFilter scenarioArgumentFilter,
			SecurityContext securityContext) {
		baseclassNewService.validateFilter(scenarioArgumentFilter,securityContext);

	}

	public void validate(ScenarioCreate creationContainer,
			SecurityContext securityContext) {
		baseclassNewService.validateCreate(creationContainer,securityContext);
		String evaluatingJSCodeId = creationContainer.getEvaluatingJSCodeId();
		FileResource executionParametersHolder = evaluatingJSCodeId != null
				? getByIdOrNull(evaluatingJSCodeId, FileResource.class, null,
						securityContext) : null;
		if (executionParametersHolder == null && evaluatingJSCodeId != null) {
			throw new BadRequestException("No FileResource with id " + evaluatingJSCodeId);
		}
		creationContainer.setEvaluatingJSCode(executionParametersHolder);


	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public Scenario createScenario(ScenarioCreate creationContainer,
			SecurityContext securityContext) {
		List<Object> toMerge = new ArrayList<>();
		File log = new File(FileResourceService.generateNewPathForFileResource(
				"scenario-log-", securityContext.getUser()) + ".log");
		FileResource fileResource = fileResourceService.createDontPersist(
				log.getAbsolutePath(), securityContext);
		toMerge.add(fileResource);
		creationContainer.setLogFileResource(fileResource);
		Scenario scenario = createScenarioNoMerge(creationContainer,
				securityContext);
		toMerge.add(scenario);

		repository.massMerge(toMerge);
		return scenario;

	}

	public Scenario updateScenario(ScenarioUpdate creationContainer,
			SecurityContext securityContext) {
		Scenario scenario = creationContainer.getScenario();
		if (updateScenarioNoMerge(scenario, creationContainer)) {
			repository.merge(scenario);

		}
		return scenario;

	}

	private Scenario createScenarioNoMerge(ScenarioCreate creationContainer,
			SecurityContext securityContext) {
		Scenario scenario = new Scenario(creationContainer.getName(),
				securityContext);
		updateScenarioNoMerge(scenario, creationContainer);
		return scenario;
	}

	private boolean updateScenarioNoMerge(Scenario scenario,
			ScenarioCreate creationContainer) {
		boolean update = baseclassNewService.updateBaseclassNoMerge(creationContainer,scenario);
		if (creationContainer.getScenarioHint() != null
				&& !creationContainer.getScenarioHint().equals(
						scenario.getScenarioHint())) {
			scenario.setScenarioHint(creationContainer.getScenarioHint());
			update = true;
		}

		if (creationContainer.getEvaluatingJSCode() != null && (scenario.getEvaluatingJSCode() == null || !creationContainer.getEvaluatingJSCode().getId().equals(scenario.getEvaluatingJSCode().getId()))) {
			scenario.setEvaluatingJSCode(creationContainer.getEvaluatingJSCode());
			update = true;
		}

		if (creationContainer.getLogFileResource() != null && (scenario.getLogFileResource() == null || !creationContainer.getLogFileResource().equals(scenario.getLogFileResource()))) {
			scenario.setLogFileResource(creationContainer.getLogFileResource());
			update = true;
		}
		return update;

	}

	public PaginationResponse<Scenario> getAllScenarios(ScenarioFilter filter,
			SecurityContext securityContext) {
		List<Scenario> list = repository.listAllScenarios(filter,
				securityContext);
		long count = repository.countAllScenarios(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}

	public void validate(ClearLogRequest clearLogRequest,
			SecurityContext securityContext) {
		Scenario scenario = clearLogRequest.getScenarioId() != null ? getByIdOrNull(clearLogRequest.getScenarioId(), Scenario.class, null, securityContext) : null;
		if (scenario == null) {
			throw new BadRequestException("No Scenario with id " + clearLogRequest.getScenarioId());
		}
		clearLogRequest.setScenario(scenario);
	}

	public void clearLog(ClearLogRequest creationContainer,
			SecurityContext securityContext) {
		LogHolder.clearLogger(creationContainer.getScenario().getId(),creationContainer.getScenario().getLogFileResource().getFullPath());
	}

}
