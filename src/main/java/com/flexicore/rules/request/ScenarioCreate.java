package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;

public class ScenarioCreate {

    private String name;
    private String description;
    private String ruleId;
    @JsonIgnore
    private FlexiCoreRule flexiCoreRule;

    public String getName() {
        return name;
    }

    public <T extends ScenarioCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends ScenarioCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getRuleId() {
        return ruleId;
    }

    public <T extends ScenarioCreate> T setRuleId(String ruleId) {
        this.ruleId = ruleId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRule getFlexiCoreRule() {
        return flexiCoreRule;
    }

    public <T extends ScenarioCreate> T setFlexiCoreRule(FlexiCoreRule flexiCoreRule) {
        this.flexiCoreRule = flexiCoreRule;
        return (T) this;
    }
}
