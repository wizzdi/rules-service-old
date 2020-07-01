package com.flexicore.rules.interfaces;

import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.rules.model.ScenarioTriggerType;
import com.flexicore.rules.request.ScenarioTriggerTypeCreate;
import com.flexicore.rules.request.ScenarioTriggerTypeFilter;
import com.flexicore.rules.request.ScenarioTriggerTypeUpdate;
import com.flexicore.security.SecurityContext;

public interface IScenarioTriggerTypeService extends ServicePlugin {
    void validate(ScenarioTriggerTypeFilter scenarioTriggerTypeArgumentFilter, SecurityContext securityContext);

    void validate(ScenarioTriggerTypeCreate creationContainer, SecurityContext securityContext);

    ScenarioTriggerType createScenarioTriggerType(ScenarioTriggerTypeCreate creationContainer, SecurityContext securityContext);

    ScenarioTriggerType updateScenarioTriggerType(ScenarioTriggerTypeUpdate creationContainer, SecurityContext securityContext);

    ScenarioTriggerType createScenarioTriggerTypeNoMerge(ScenarioTriggerTypeCreate creationContainer, SecurityContext securityContext);

    boolean updateScenarioTriggerTypeNoMerge(ScenarioTriggerType scenarioTriggerType, ScenarioTriggerTypeCreate creationContainer);

    PaginationResponse<ScenarioTriggerType> getAllScenarioTriggerTypes(ScenarioTriggerTypeFilter filter, SecurityContext securityContext);
}
