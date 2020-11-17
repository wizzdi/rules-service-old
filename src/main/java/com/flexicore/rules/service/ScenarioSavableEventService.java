package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.rules.events.ScenarioSavableEvent;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.repository.ScenarioSavableEventRepository;
import com.flexicore.rules.repository.ScenarioSavableEventRepository;
import com.flexicore.rules.request.ScenarioSavableEventCreate;
import com.flexicore.rules.request.ScenarioSavableEventFilter;
import com.flexicore.rules.request.ScenarioSavableEventUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNoSQLService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import org.slf4j.Logger;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioSavableEventService implements ServicePlugin {


	@Autowired
	@PluginInfo(version = 1)
	private ScenarioSavableEventRepository scenarioSavableEventRepository;

	@Autowired
	private BaseclassNoSQLService baseclassNoSQLService;

	public void validate(ScenarioSavableEventFilter scenarioSavableEventArgumentFilter,
						 SecurityContext securityContext) {



	}

	public void validate(ScenarioSavableEventCreate creationContainer,
			SecurityContext securityContext) {


	}

	public ScenarioSavableEvent createScenarioSavableEvent(ScenarioSavableEventCreate creationContainer,
											 SecurityContext securityContext) {

		ScenarioSavableEvent scenarioSavableEvent = createScenarioSavableEventNoMerge(creationContainer,
				securityContext);

		scenarioSavableEventRepository.mergeScenarioSavableEvent(scenarioSavableEvent);
		return scenarioSavableEvent;

	}

	public ScenarioSavableEvent updateScenarioSavableEvent(ScenarioSavableEventUpdate creationContainer,
											 SecurityContext securityContext) {
		ScenarioSavableEvent scenarioSavableEvent = creationContainer.getScenarioSavableEvent();
		if (updateScenarioSavableEventNoMerge(scenarioSavableEvent, creationContainer)) {
			scenarioSavableEventRepository.mergeScenarioSavableEvent(scenarioSavableEvent);

		}
		return scenarioSavableEvent;

	}

	private ScenarioSavableEvent createScenarioSavableEventNoMerge(ScenarioSavableEventCreate creationContainer,
			SecurityContext securityContext) {
		ScenarioSavableEvent scenarioSavableEvent = new ScenarioSavableEvent();
		updateScenarioSavableEventNoMerge(scenarioSavableEvent, creationContainer);
		return scenarioSavableEvent;
	}

	private boolean updateScenarioSavableEventNoMerge(ScenarioSavableEvent scenarioSavableEvent,
			ScenarioSavableEventCreate creationContainer) {
		boolean update = baseclassNoSQLService.updateBaseclassNoSQLNoMerge(scenarioSavableEvent, creationContainer);
		if (creationContainer.get() != null && !creationContainer.get().isEmpty()) {
			Map<String, Object> jsonNode = scenarioSavableEvent.get();
			if (jsonNode == null) {
				scenarioSavableEvent.setMore(creationContainer.get());
				update = true;
			} else {
				for (Map.Entry<String, Object> entry : creationContainer.get().entrySet()) {
					String key = entry.getKey();
					Object newVal = entry.getValue();
					Object val = jsonNode.get(key);
					if (newVal!=null&&!newVal.equals(val)) {
						jsonNode.put(key, newVal);
						update = true;
					}
				}
			}


		}

		return update;

	}

	public PaginationResponse<ScenarioSavableEvent> getAllScenarioSavableEvents(ScenarioSavableEventFilter filter,
																  SecurityContext securityContext) {
		List<ScenarioSavableEvent> list = listAllScenarioSavableEvents(filter);
		long count = scenarioSavableEventRepository.countAllScenarioSavableEvents(filter);
		return new PaginationResponse<>(list, filter, count);
	}

	public List<ScenarioSavableEvent> listAllScenarioSavableEvents(ScenarioSavableEventFilter filter) {
		return scenarioSavableEventRepository.getAllScenarioSavableEvents(filter);
	}

	public void mergeScenarioSavableEvent(ScenarioSavableEvent o) {
		scenarioSavableEventRepository.mergeScenarioSavableEvent(o);
	}



}
