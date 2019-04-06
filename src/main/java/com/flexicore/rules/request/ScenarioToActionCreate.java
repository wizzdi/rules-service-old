package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioAction;

public class ScenarioToActionCreate {
    private String name;
    private String description;
    private String scenarioId;
    private String actionId;
    @JsonIgnore
    private Scenario scenario;
    @JsonIgnore
    private ScenarioAction scenarioAction;

    public String getName() {
        return name;
    }

    public <T extends ScenarioToActionCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends ScenarioToActionCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public <T extends ScenarioToActionCreate> T setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return (T) this;
    }

    public String getActionId() {
        return actionId;
    }

    public <T extends ScenarioToActionCreate> T setActionId(String actionId) {
        this.actionId = actionId;
        return (T) this;
    }

    @JsonIgnore
    public Scenario getScenario() {
        return scenario;
    }


    public <T extends ScenarioToActionCreate> T setScenario(Scenario scenario) {
        this.scenario = scenario;
        return (T) this;
    }

    @JsonIgnore
    public ScenarioAction getScenarioAction() {
        return scenarioAction;
    }

    public <T extends ScenarioToActionCreate> T setScenarioAction(ScenarioAction scenarioAction) {
        this.scenarioAction = scenarioAction;
        return (T) this;
    }
}
