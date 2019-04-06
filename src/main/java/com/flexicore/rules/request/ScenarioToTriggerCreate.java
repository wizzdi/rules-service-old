package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioAction;
import com.flexicore.rules.model.ScenarioTrigger;

public class ScenarioToTriggerCreate {
    private String name;
    private String description;
    private String scenarioId;
    private String triggerId;
    @JsonIgnore
    private Scenario scenario;
    @JsonIgnore
    private ScenarioTrigger scenarioTrigger;

    public String getName() {
        return name;
    }

    public <T extends ScenarioToTriggerCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends ScenarioToTriggerCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public <T extends ScenarioToTriggerCreate> T setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return (T) this;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public <T extends ScenarioToTriggerCreate> T setTriggerId(String triggerId) {
        this.triggerId = triggerId;
        return (T) this;
    }

    @JsonIgnore
    public Scenario getScenario() {
        return scenario;
    }


    public <T extends ScenarioToTriggerCreate> T setScenario(Scenario scenario) {
        this.scenario = scenario;
        return (T) this;
    }

    @JsonIgnore
    public ScenarioTrigger getScenarioTrigger() {
        return scenarioTrigger;
    }

    public <T extends ScenarioToTriggerCreate> T setScenarioTrigger(ScenarioTrigger scenarioTrigger) {
        this.scenarioTrigger = scenarioTrigger;
        return (T) this;
    }
}
