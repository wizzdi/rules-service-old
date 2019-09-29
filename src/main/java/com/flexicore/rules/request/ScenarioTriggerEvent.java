package com.flexicore.rules.request;

import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.security.SecurityContext;

public class ScenarioTriggerEvent {

    private SecurityContext securityContext;
    private ScenarioTrigger scenarioTrigger;

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public <T extends ScenarioTriggerEvent> T setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        return (T) this;
    }

    public ScenarioTrigger getScenarioTrigger() {
        return scenarioTrigger;
    }

    public <T extends ScenarioTriggerEvent> T setScenarioTrigger(ScenarioTrigger scenarioTrigger) {
        this.scenarioTrigger = scenarioTrigger;
        return (T) this;
    }

    @Override
    public String toString() {
        return "ScenarioTriggerEvent{" +
                "scenarioTrigger=" + scenarioTrigger +
                '}';
    }
}
