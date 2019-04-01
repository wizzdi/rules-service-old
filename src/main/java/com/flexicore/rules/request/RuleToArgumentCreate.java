package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleArgument;

public class RuleToArgumentCreate {
    private String name;
    private String description;
    private Integer ordinal;
    private String flexiCoreRuleId;
    @JsonIgnore
    private FlexiCoreRule flexiCoreRule;
    private String flexicoreRuleArgumentId;
    @JsonIgnore
    private FlexiCoreRuleArgument flexiCoreRuleArgument;

    public String getName() {
        return name;
    }

    public <T extends RuleToArgumentCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends RuleToArgumentCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getFlexiCoreRuleId() {
        return flexiCoreRuleId;
    }

    public <T extends RuleToArgumentCreate> T setFlexiCoreRuleId(String flexiCoreRuleId) {
        this.flexiCoreRuleId = flexiCoreRuleId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRule getFlexiCoreRule() {
        return flexiCoreRule;
    }

    public <T extends RuleToArgumentCreate> T setFlexiCoreRule(FlexiCoreRule flexiCoreRule) {
        this.flexiCoreRule = flexiCoreRule;
        return (T) this;
    }

    public String getFlexicoreRuleArgumentId() {
        return flexicoreRuleArgumentId;
    }

    public <T extends RuleToArgumentCreate> T setFlexicoreRuleArgumentId(String flexicoreRuleArgumentId) {
        this.flexicoreRuleArgumentId = flexicoreRuleArgumentId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRuleArgument getFlexiCoreRuleArgument() {
        return flexiCoreRuleArgument;
    }

    public <T extends RuleToArgumentCreate> T setFlexiCoreRuleArgument(FlexiCoreRuleArgument flexiCoreRuleArgument) {
        this.flexiCoreRuleArgument = flexiCoreRuleArgument;
        return (T) this;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public <T extends RuleToArgumentCreate> T setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
        return (T) this;
    }
}
