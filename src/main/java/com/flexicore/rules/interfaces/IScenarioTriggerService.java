package com.flexicore.rules.interfaces;

import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.request.ScenarioTriggerCreate;
import com.flexicore.security.SecurityContext;

public interface IScenarioTriggerService extends ServicePlugin {
    void validate(ScenarioTriggerCreate creationContainer, SecurityContext securityContext);

    ScenarioTrigger createScenarioTriggerNoMerge(ScenarioTriggerCreate creationContainer, SecurityContext securityContext);

    boolean updateScenarioTriggerNoMerge(ScenarioTrigger scenarioTrigger, ScenarioTriggerCreate creationContainer);
}
