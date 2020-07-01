package com.flexicore.rules.events;

import com.flexicore.model.nosql.BaseclassNoSQL;


public class ScenarioEvent extends BaseclassNoSQL {

    private String evaluatedScenarioTriggerId;


    public String getEvaluatedScenarioTriggerId() {
        return evaluatedScenarioTriggerId;
    }

    public <T extends ScenarioEvent> T setEvaluatedScenarioTriggerId(String evaluatedScenarioTriggerId) {
        this.evaluatedScenarioTriggerId = evaluatedScenarioTriggerId;
        return (T) this;
    }
}
