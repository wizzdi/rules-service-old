package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.ScenarioTrigger;

public class FireScenarioTrigger {

    private String scenarioTriggerId;
    @JsonIgnore
    private ScenarioTrigger scenarioTrigger;

    public String getScenarioTriggerId() {
        return scenarioTriggerId;
    }

    public <T extends FireScenarioTrigger> T setScenarioTriggerId(String scenarioTriggerId) {
        this.scenarioTriggerId = scenarioTriggerId;
        return (T) this;
    }

    @JsonIgnore
    public ScenarioTrigger getScenarioTrigger() {
        return scenarioTrigger;
    }

    public <T extends FireScenarioTrigger> T setScenarioTrigger(ScenarioTrigger scenarioTrigger) {
        this.scenarioTrigger = scenarioTrigger;
        return (T) this;
    }
}
