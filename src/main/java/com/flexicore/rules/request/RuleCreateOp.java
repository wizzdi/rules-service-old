package com.flexicore.rules.request;

import com.flexicore.rules.model.RuleOpType;

public class RuleCreateOp {
    private String name;
    private String description;

    private RuleOpType ruleOpType;

    public RuleOpType getRuleOpType() {
        return ruleOpType;
    }

    public <T extends RuleCreateOp> T setRuleOpType(RuleOpType ruleOpType) {
        this.ruleOpType = ruleOpType;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends RuleCreateOp> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends RuleCreateOp> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }
}
