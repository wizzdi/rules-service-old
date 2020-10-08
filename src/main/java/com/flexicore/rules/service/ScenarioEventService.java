package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.repository.ScenarioEventRepository;
import com.flexicore.rules.request.ScenarioEventCreate;
import com.flexicore.rules.request.ScenarioEventFilter;
import com.flexicore.rules.request.ScenarioEventUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNoSQLService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioEventService implements ServicePlugin {


	@Autowired
	@PluginInfo(version = 1)
	private ScenarioEventRepository scenarioEventRepository;

	@Autowired
	@PluginInfo(version = 1)
	private ScenarioTriggerService baseclassService;
	@Autowired
	private BaseclassNoSQLService baseclassNoSQLService;


	@Autowired
	private Logger logger;

	public void validate(ScenarioEventFilter scenarioEventArgumentFilter,
			SecurityContext securityContext) {
		Set<String> evaluatedScenarioTriggerIds=scenarioEventArgumentFilter.getEvaluatedScenarioTriggerIds();
		Map<String, ScenarioTrigger> triggers=evaluatedScenarioTriggerIds.isEmpty()?new HashMap<>(): baseclassService.listByIds(ScenarioTrigger.class,evaluatedScenarioTriggerIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(), f->f));
		evaluatedScenarioTriggerIds.removeAll(triggers.keySet());
		if(!evaluatedScenarioTriggerIds.isEmpty()){
			throw new BadRequestException("No ScenarioTriggers with ids "+evaluatedScenarioTriggerIds);
		}
		scenarioEventArgumentFilter.setScenarioTriggers(new ArrayList<>(triggers.values()));

	}

	public void validate(ScenarioEventCreate creationContainer,
			SecurityContext securityContext) {


	}

	public ScenarioEvent createScenarioEvent(ScenarioEventCreate creationContainer,
											 SecurityContext securityContext) {

		ScenarioEvent scenarioEvent = createScenarioEventNoMerge(creationContainer,
				securityContext);

		scenarioEventRepository.mergeScenarioEvent(scenarioEvent);
		return scenarioEvent;

	}

	public ScenarioEvent updateScenarioEvent(ScenarioEventUpdate creationContainer,
											 SecurityContext securityContext) {
		ScenarioEvent scenarioEvent = creationContainer.getScenarioEvent();
		if (updateScenarioEventNoMerge(scenarioEvent, creationContainer)) {
			scenarioEventRepository.mergeScenarioEvent(scenarioEvent);

		}
		return scenarioEvent;

	}

	private ScenarioEvent createScenarioEventNoMerge(ScenarioEventCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioEvent scenarioEvent = new ScenarioEvent();
		updateScenarioEventNoMerge(scenarioEvent, creationContainer);
		return scenarioEvent;
	}

	private boolean updateScenarioEventNoMerge(ScenarioEvent scenarioEvent,
			ScenarioEventCreate creationContainer) {
		boolean update = baseclassNoSQLService.updateBaseclassNoSQLNoMerge(scenarioEvent, creationContainer);
		if (creationContainer.getEvaluatedScenarioTriggerIds() != null && !creationContainer.getEvaluatedScenarioTriggerIds().equals(scenarioEvent.getEvaluatedScenarioTriggerIds())) {
			scenarioEvent.setEvaluatedScenarioTriggerIds(creationContainer.getEvaluatedScenarioTriggerIds());
			update = true;
		}

		return update;

	}

	public PaginationResponse<ScenarioEvent> getAllScenarioEvents(ScenarioEventFilter filter,
			SecurityContext securityContext) {
		List<ScenarioEvent> list = scenarioEventRepository.getAllScenarioEvents(filter);
		long count = scenarioEventRepository.countAllScenarioEvents(filter);
		return new PaginationResponse<>(list, filter, count);
	}

	public void mergeScenarioEvent(ScenarioEvent o) {
		scenarioEventRepository.mergeScenarioEvent(o);
	}

	public List<ScenarioEvent> listByIds(Set<String> scenarioEventIds) {
		return scenarioEventRepository.listByIds(ScenarioEvent.class,scenarioEventIds);
	}


}
