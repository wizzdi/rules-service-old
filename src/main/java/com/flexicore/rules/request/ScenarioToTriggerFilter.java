package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.ScenarioTrigger;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Currently extends FilteringInformationHolder with no additional fields ")
public class ScenarioToTriggerFilter extends FilteringInformationHolder {

    @JsonIgnore
    private List<ScenarioTrigger> scenarioTriggers;

    private Boolean enabled;

    @JsonIgnore
    public List<ScenarioTrigger> getScenarioTriggers() {
        return scenarioTriggers;
    }

    public <T extends ScenarioToTriggerFilter> T setScenarioTriggers(List<ScenarioTrigger> scenarioTriggers) {
        this.scenarioTriggers = scenarioTriggers;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends ScenarioToTriggerFilter> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
