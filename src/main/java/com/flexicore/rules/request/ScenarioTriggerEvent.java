package com.flexicore.rules.request;

import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.security.SecurityContext;

public class ScenarioTriggerEvent<E extends ScenarioTrigger> {

    private SecurityContext securityContext;
    private E scenarioTrigger;

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public <T extends ScenarioTriggerEvent<E>> T setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        return (T) this;
    }

    public E getScenarioTrigger() {
        return scenarioTrigger;
    }

    public <T extends ScenarioTriggerEvent<E>> T setScenarioTrigger(E scenarioTrigger) {
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
