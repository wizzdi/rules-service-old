package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EvaluateRuleRequest",description = "defines a rule to be evaluated, uses previously retrieved RuleId ")
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
