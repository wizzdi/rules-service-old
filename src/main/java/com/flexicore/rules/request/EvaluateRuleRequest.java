package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;

public class EvaluateRuleRequest {
    private String ruleId;
    @JsonIgnore
    private FlexiCoreRule rule;

    public String getRuleId() {
        return ruleId;
    }

    public <T extends EvaluateRuleRequest> T setRuleId(String ruleId) {
        this.ruleId = ruleId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRule getRule() {
        return rule;
    }

    public <T extends EvaluateRuleRequest> T setRule(FlexiCoreRule rule) {
        this.rule = rule;
        return (T) this;
    }
}
