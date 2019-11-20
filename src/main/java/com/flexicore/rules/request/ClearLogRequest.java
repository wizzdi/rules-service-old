package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;

public class ClearLogRequest {

    private String ruleId;
    @JsonIgnore
    private FlexiCoreRule flexiCoreRule;

    public String getRuleId() {
        return ruleId;
    }

    public <T extends ClearLogRequest> T setRuleId(String ruleId) {
        this.ruleId = ruleId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRule getFlexiCoreRule() {
        return flexiCoreRule;
    }

    public <T extends ClearLogRequest> T setFlexiCoreRule(FlexiCoreRule flexiCoreRule) {
        this.flexiCoreRule = flexiCoreRule;
        return (T) this;
    }
}
