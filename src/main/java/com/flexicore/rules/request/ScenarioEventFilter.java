package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassNoSQLFilter;
import com.flexicore.rules.model.ScenarioTrigger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScenarioEventFilter extends BaseclassNoSQLFilter {

    private Set<String> evaluatedScenarioTriggerIds=new HashSet<>();
    @JsonIgnore
    private List<ScenarioTrigger> scenarioTriggers;

    public Set<String> getEvaluatedScenarioTriggerIds() {
        return evaluatedScenarioTriggerIds;
    }

    public <T extends ScenarioEventFilter> T setEvaluatedScenarioTriggerIds(Set<String> evaluatedScenarioTriggerIds) {
        this.evaluatedScenarioTriggerIds = evaluatedScenarioTriggerIds;
        return (T) this;
    }

    @JsonIgnore
    public List<ScenarioTrigger> getScenarioTriggers() {
        return scenarioTriggers;
    }

    public <T extends ScenarioEventFilter> T setScenarioTriggers(List<ScenarioTrigger> scenarioTriggers) {
        this.scenarioTriggers = scenarioTriggers;
        return (T) this;
    }
}
