package com.flexicore.rules.invokers;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.interfaces.dynamic.ListingInvoker;
import com.flexicore.rules.events.ScenarioSavableEvent;
import com.flexicore.rules.request.ScenarioSavableEventFilter;
import com.flexicore.rules.service.ScenarioSavableEventService;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;

import javax.inject.Inject;

@PluginInfo(version = 1)
@InvokerInfo
@Extension
public class ScenarioSavableEventInvoker implements ListingInvoker<ScenarioSavableEvent, ScenarioSavableEventFilter> {

    @Inject
    @PluginInfo(version = 1)
    private ScenarioSavableEventService scenarioSavableEventService;

    @Override
    @InvokerMethodInfo(displayName = "listAllScenarioSavableEvents",description = "lists all scenarioSavableEvents")
    public PaginationResponse<ScenarioSavableEvent> listAll(ScenarioSavableEventFilter filter, SecurityContext securityContext) {
        scenarioSavableEventService.validate(filter,securityContext);
        return scenarioSavableEventService.getAllScenarioSavableEvents(filter,securityContext);
    }

    @Override
    public Class<ScenarioSavableEventFilter> getFilterClass() {
        return ScenarioSavableEventFilter.class;
    }

    @Override
    public Class<?> getHandlingClass() {
        return ScenarioSavableEvent.class;
    }
}
