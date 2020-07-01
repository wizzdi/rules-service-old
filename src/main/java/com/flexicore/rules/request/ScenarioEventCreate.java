package com.flexicore.rules.request;

import com.flexicore.request.BaseclassNoSQLCreate;

public class ScenarioEventCreate extends BaseclassNoSQLCreate {

    private String evaluatedScenarioTriggerId;

    public String getEvaluatedScenarioTriggerId() {
        return evaluatedScenarioTriggerId;
    }

    public <T extends ScenarioEventCreate> T setEvaluatedScenarioTriggerId(String evaluatedScenarioTriggerId) {
        this.evaluatedScenarioTriggerId = evaluatedScenarioTriggerId;
        return (T) this;
    }
}
