package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.RuleToArgument;

public class RuleToArgumentUpdate extends RuleToArgumentCreate{
    private String id;
    @JsonIgnore
    private RuleToArgument RuleToArgument;

    public String getId() {
        return id;
    }

    public <T extends RuleToArgumentUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public RuleToArgument getRuleToArgument() {
        return RuleToArgument;
    }

    public <T extends RuleToArgumentUpdate> T setRuleToArgument(RuleToArgument RuleToArgument) {
        this.RuleToArgument = RuleToArgument;
        return (T) this;
    }
}
