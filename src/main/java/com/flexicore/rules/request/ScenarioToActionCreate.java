package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioAction;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Container used for the creation of ScenarioToActionCreate a ScenarioToActionCreate creates Many To Many Link between an Action and Scenario" +
        "This means that ScenarioAction can be reused across multiple Scenarios, A Scenario can fire multiple ScenarioActions")
public class ScenarioToActionCreate {
    private String name;
    private String description;
    private String scenarioId;
    private String actionId;
    @JsonIgnore
    private Scenario scenario;
    @JsonIgnore
    private ScenarioAction scenarioAction;
    private Boolean enabled;

    public String getName() {
        return name;
    }
    @Schema(description = "Human readable name")
    public <T extends ScenarioToActionCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }
    @Schema(description = "Human readable description")
    public <T extends ScenarioToActionCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getScenarioId() {
        return scenarioId;
    }
    @Schema(description = "A valid system ID of the Scenario",required = false)
    public <T extends ScenarioToActionCreate> T setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return (T) this;
    }

    public String getActionId() {
        return actionId;
    }
    @Schema(description = "A valid system ID of the Action",required = false)
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

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends ScenarioToActionCreate> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
