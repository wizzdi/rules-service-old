package com.flexicore.rules.events;

import com.flexicore.model.nosql.BaseclassNoSQL;

import java.util.Set;


public class ScenarioEvent extends BaseclassNoSQL {

    private Set<String> evaluatedScenarioTriggerIds;


    public Set<String> getEvaluatedScenarioTriggerIds() {
        return evaluatedScenarioTriggerIds;
    }

    public <T extends ScenarioEvent> T setEvaluatedScenarioTriggerIds(Set<String> evaluatedScenarioTriggerIds) {
        this.evaluatedScenarioTriggerIds = evaluatedScenarioTriggerIds;
        return (T) this;
    }
}
