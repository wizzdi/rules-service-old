package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.repository.ScenarioRepository;
import com.flexicore.rules.request.*;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.FileResourceService;

import javax.ws.rs.BadRequestException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.flexicore.service.SecurityService;
import org.pf4j.Extension;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

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
	private Logger logger;
	@Autowired
	private SecurityService securityService;

	@EventListener
	@Async
	public void handleTrigger(PluginsLoadedEvent pluginsLoadedEvent) {
		SecurityContext securityContext=securityService.getAdminUserSecurityContext();
		List<Scenario> scenarios=repository.listAllScenarios(new ScenarioFilter().setNoLogs(true),null);
		List<Object> toMerge=new ArrayList<>();
		for (Scenario scenario : scenarios) {
			FileResource fileResource=createScenarioLogFileNoMerge(securityContext);
			scenario.setLogFileResource(fileResource);
			toMerge.add(fileResource);
			toMerge.add(scenario);
		}
		repository.massMerge(toMerge);
		if(!scenarios.isEmpty()){
			logger.info("created logs for "+scenarios.size() +" scenarios");
		}
	}

	public void validate(ScenarioFilter scenarioArgumentFilter,
			SecurityContext securityContext) {

	}

	public void validate(ScenarioCreate creationContainer,
			SecurityContext securityContext) {
		String ruleId = creationContainer.getRuleId();
		FlexiCoreRule executionParametersHolder = ruleId != null
				? getByIdOrNull(ruleId, FlexiCoreRule.class, null,
						securityContext) : null;
		if (executionParametersHolder == null && ruleId != null) {
			throw new BadRequestException("No FlexiCoreRule with id " + ruleId);
		}
		creationContainer.setFlexiCoreRule(executionParametersHolder);

		String actionManagerScriptId = creationContainer
				.getActionManagerScriptId();
		FileResource actionManagerScript = actionManagerScriptId != null
				? getByIdOrNull(actionManagerScriptId, FileResource.class,
						null, securityContext) : null;
		if (actionManagerScript == null && actionManagerScriptId != null) {
			throw new BadRequestException("No FileResource with id "
					+ actionManagerScriptId);
		}
		creationContainer.setActionManagerScript(actionManagerScript);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public Scenario createScenario(ScenarioCreate creationContainer,
			SecurityContext securityContext) {
		List<Object> toMerge = new ArrayList<>();
		FileResource fileResource = createScenarioLogFileNoMerge(securityContext);
		toMerge.add(fileResource);
		creationContainer.setLogFileResource(fileResource);
		Scenario scenario = createScenarioNoMerge(creationContainer,
				securityContext);
		toMerge.add(scenario);

		repository.massMerge(toMerge);
		return scenario;

	}

	public FileResource createScenarioLogFileNoMerge(SecurityContext securityContext) {
		File log = new File(FileResourceService.generateNewPathForFileResource(
				"scenario-log-", securityContext.getUser()) + ".log");
		FileResource fileResource = fileResourceService.createDontPersist(
				log.getAbsolutePath(), securityContext);
		return fileResource;
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
		boolean update = false;
		if (creationContainer.getName() != null
				&& !creationContainer.getName().equals(scenario.getName())) {
			scenario.setName(creationContainer.getName());
			update = true;
		}

		if (creationContainer.getDescription() != null
				&& !creationContainer.getDescription().equals(
						scenario.getDescription())) {
			scenario.setDescription(creationContainer.getDescription());
			update = true;
		}
		if (creationContainer.getScenarioHint() != null
				&& !creationContainer.getScenarioHint().equals(
						scenario.getScenarioHint())) {
			scenario.setScenarioHint(creationContainer.getScenarioHint());
			update = true;
		}

		if (creationContainer.getFlexiCoreRule() != null
				&& (scenario.getFlexiCoreRule() == null || !creationContainer
						.getFlexiCoreRule().getId()
						.equals(scenario.getFlexiCoreRule().getId()))) {
			scenario.setFlexiCoreRule(creationContainer.getFlexiCoreRule());
			update = true;
		}

		if (creationContainer.getActionManagerScript() != null
				&& (scenario.getActionManagerScript() == null || !creationContainer
						.getActionManagerScript().getId()
						.equals(scenario.getActionManagerScript().getId()))) {
			scenario.setActionManagerScript(creationContainer
					.getActionManagerScript());
			update = true;
		}
		if (creationContainer.getLogFileResource() != null
				&& (scenario.getLogFileResource() == null || !creationContainer
						.getLogFileResource().equals(
								scenario.getLogFileResource()))) {
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
		Scenario scenario = clearLogRequest.getScenarioId() != null
				? getByIdOrNull(clearLogRequest.getScenarioId(),
						Scenario.class, null, securityContext) : null;
		if (scenario == null) {
			throw new BadRequestException("No Scenario with id "
					+ clearLogRequest.getScenarioId());
		}
		clearLogRequest.setScenario(scenario);
	}

	public void clearLog(ClearLogRequest creationContainer,
			SecurityContext securityContext) {
		LogHolder.clearLogger(creationContainer.getScenario());
	}

}
