package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.model.ScenarioToTrigger;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Container for updating a ScenarioToTrigger link")
public class ScenarioToTriggerUpdate extends ScenarioToTriggerCreate{
    private String id;
    @JsonIgnore
    private ScenarioToTrigger scenarioToTrigger;

    public String getId() {
        return id;
    }
@Schema(description = "A valid ID of an existing trigger")
    public <T extends ScenarioToTriggerUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public ScenarioToTrigger getScenarioToTrigger() {
        return scenarioToTrigger;
    }

    public <T extends ScenarioToTriggerUpdate> T setScenarioToTrigger(ScenarioToTrigger scenarioToTrigger) {
        this.scenarioToTrigger = scenarioToTrigger;
        return (T) this;
    }
}
