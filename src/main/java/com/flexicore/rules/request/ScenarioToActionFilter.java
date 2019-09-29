package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.Scenario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "ScenarioToActionFilter directly extends FilteringInformationHolder")
public class ScenarioToActionFilter extends FilteringInformationHolder {

    @JsonIgnore
    private List<Scenario> scenarios;
    private Boolean enabled;

    @JsonIgnore
    public List<Scenario> getScenarios() {
        return scenarios;
    }

    public <T extends ScenarioToActionFilter> T setScenarios(List<Scenario> scenarios) {
        this.scenarios = scenarios;
        return (T) this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public <T extends ScenarioToActionFilter> T setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return (T) this;
    }
}
