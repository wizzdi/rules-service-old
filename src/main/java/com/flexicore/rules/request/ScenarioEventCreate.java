package com.flexicore.rules.request;

import com.flexicore.request.BaseclassNoSQLCreate;

import java.util.Set;

public class ScenarioEventCreate extends BaseclassNoSQLCreate {

    private Set<String> evaluatedScenarioTriggerIds;

    public Set<String> getEvaluatedScenarioTriggerIds() {
        return evaluatedScenarioTriggerIds;
    }

    public <T extends ScenarioEventCreate> T setEvaluatedScenarioTriggerIds(Set<String> evaluatedScenarioTriggerIds) {
        this.evaluatedScenarioTriggerIds = evaluatedScenarioTriggerIds;
        return (T) this;
    }
}
