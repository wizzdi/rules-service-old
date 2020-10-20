package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.events.GenericTriggerScenarioEvent;
import com.flexicore.rules.model.GenericTrigger;
import com.flexicore.rules.model.GenericTriggerRequest;
import com.flexicore.rules.repository.GenericTriggerRepository;
import com.flexicore.rules.request.GenericTriggerCreate;
import com.flexicore.rules.request.GenericTriggerEvent;
import com.flexicore.rules.request.GenericTriggerFilter;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.flexicore.service.SecurityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

@PluginInfo(version = 1)
@Extension
@Component
public class GenericTriggerService implements ServicePlugin {

	@Autowired
	private ApplicationEventPublisher genericTriggerEvent;

	@PluginInfo(version = 1)
	@Autowired
	private GenericTriggerRepository genericTriggerRepository;

	@Autowired
	private BaseclassNewService baseclassNewService;

	@Autowired
	private SecurityService securityService;
	private static final AtomicBoolean init = new AtomicBoolean(false);
	private static GenericTrigger genericTrigger;

	@EventListener
	public void init(PluginsLoadedEvent pluginsLoadedEvent) {
		if (init.compareAndSet(false, true)) {
			SecurityContext securityContext = securityService
					.getAdminUserSecurityContext();
			List<GenericTrigger> existing = genericTriggerRepository
					.listAllGenericTriggers(new GenericTriggerFilter()
							.setPageSize(1).setCurrentPage(0), securityContext);
			if (existing.isEmpty()) {
				GenericTriggerCreate generictriggerCreate = new GenericTriggerCreate()
						.setName("Generic Trigger").setDescription(
								"Generic Trigger");
				genericTrigger = createGenericTrigger(generictriggerCreate,
						securityContext);
			} else {
				genericTrigger = existing.get(0);
			}
		}
	}

	public GenericTrigger createGenericTrigger(
			GenericTriggerCreate generictriggerCreate,
			SecurityContext securityContext) {
		GenericTrigger genericTrigger = createGenericTriggerNoMerge(
				generictriggerCreate, securityContext);
		genericTriggerRepository.merge(genericTrigger);
		return genericTrigger;
	}

	private GenericTrigger createGenericTriggerNoMerge(
			GenericTriggerCreate generictriggerCreate,
			SecurityContext securityContext) {
		GenericTrigger genericTrigger = new GenericTrigger(
				generictriggerCreate.getName(), securityContext);
		updateGenericTriggerNoMerge(generictriggerCreate, genericTrigger);
		return genericTrigger;
	}

	private boolean updateGenericTriggerNoMerge(
			GenericTriggerCreate generictriggerCreate,
			GenericTrigger genericTrigger) {
		return baseclassNewService.updateBaseclassNoMerge(generictriggerCreate,
				genericTrigger);
	}

	public void fireGenericTrigger(GenericTriggerRequest genericTriggerRequest,
			SecurityContext securityContext) {
		GenericTriggerScenarioEvent scenarioEvent = new GenericTriggerScenarioEvent()
				.setRelatedBaseclassesIds(
						genericTriggerRequest.getBaseclasses().stream()
								.map(f -> f.getId())
								.collect(Collectors.toSet())).setUserData(
						genericTriggerRequest.getUserData());
		GenericTriggerEvent genericTriggerEvent = new GenericTriggerEvent()
				.setBaseclasses(genericTriggerRequest.getBaseclasses())
				.setUserData(genericTriggerRequest.getUserData())
				.setScenarioEvent(scenarioEvent)
				.setScenarioTrigger(genericTrigger)
				.setSecurityContext(securityContext);
		this.genericTriggerEvent.publishEvent(genericTriggerEvent);
	}

	public void validate(GenericTriggerRequest genericTriggerRequest,
			SecurityContext securityContext) {
		Set<String> baseclassIds = genericTriggerRequest.getBaseclassIds()
				.parallelStream().map(f -> f.getId())
				.collect(Collectors.toSet());
		List<Baseclass> baseclasses = baseclassIds.isEmpty()
				? new ArrayList<>()
				: genericTriggerRepository.listByIds(Baseclass.class,
						baseclassIds, securityContext);
		genericTriggerRequest.setBaseclasses(baseclasses);
	}
}
