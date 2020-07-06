package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.model.ScenarioTrigger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionReplacementFilter extends FilteringInformationHolder {

    private Set<String> scenarioTriggerIds=new HashSet<>();
    @JsonIgnore
    private List<ScenarioTrigger> scenarioTriggers;
    private Set<String> scenarioToActionIds=new HashSet<>();
    @JsonIgnore
    private List<ScenarioToAction> scenarioToActions;

    public Set<String> getScenarioTriggerIds() {
        return scenarioTriggerIds;
    }

    public <T extends ActionReplacementFilter> T setScenarioTriggerIds(Set<String> scenarioTriggerIds) {
        this.scenarioTriggerIds = scenarioTriggerIds;
        return (T) this;
    }

    @JsonIgnore
    public List<ScenarioTrigger> getScenarioTriggers() {
        return scenarioTriggers;
    }

    public <T extends ActionReplacementFilter> T setScenarioTriggers(List<ScenarioTrigger> scenarioTriggers) {
        this.scenarioTriggers = scenarioTriggers;
        return (T) this;
    }

    public Set<String> getScenarioToActionIds() {
        return scenarioToActionIds;
    }

    public <T extends ActionReplacementFilter> T setScenarioToActionIds(Set<String> scenarioToActionIds) {
        this.scenarioToActionIds = scenarioToActionIds;
        return (T) this;
    }

    @JsonIgnore
    public List<ScenarioToAction> getScenarioToActions() {
        return scenarioToActions;
    }

    public <T extends ActionReplacementFilter> T setScenarioToActions(List<ScenarioToAction> scenarioToActions) {
        this.scenarioToActions = scenarioToActions;
        return (T) this;
    }
}
