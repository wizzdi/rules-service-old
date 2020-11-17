package com.flexicore.rules.invokers;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.interfaces.dynamic.ListingInvoker;
import com.flexicore.product.model.Equipment;
import com.flexicore.product.model.EquipmentByStatusEvent;
import com.flexicore.product.model.EquipmentStatusRequest;
import com.flexicore.rules.events.ScenarioSavableEvent;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.request.ScenarioCreateEvent;
import com.flexicore.rules.request.ScenarioSavableEventCreate;
import com.flexicore.rules.request.ScenarioSavableEventFilter;
import com.flexicore.rules.service.ScenarioSavableEventService;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.util.List;

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

    @InvokerMethodInfo(displayName = "createScenarioSavableEvent", description = "create Equipment Status Event Aggregation", relatedClasses = {Scenario.class},categories = "TYPE_ACTION")
    public ScenarioSavableEvent createScenarioSavableEvent(ScenarioCreateEvent scenarioCreateEvent, SecurityContext securityContext) {
        ScenarioSavableEventCreate scenarioSavableEventCreate=new ScenarioSavableEventCreate(scenarioCreateEvent);
        scenarioSavableEventService.validate(scenarioSavableEventCreate,securityContext);
        return scenarioSavableEventService.createScenarioSavableEvent(scenarioSavableEventCreate,securityContext);
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
