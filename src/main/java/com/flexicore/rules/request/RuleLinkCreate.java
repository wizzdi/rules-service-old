package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleOp;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Rule creation container ")
public class RuleLinkCreate {
    private String ruleOpId;
    @JsonIgnore
    private FlexiCoreRuleOp flexiCoreRuleOp;
    private String ruleid;
    @JsonIgnore
    private FlexiCoreRule flexiCoreRule;

    public String getRuleOpId() {
        return ruleOpId;
    }

    public <T extends RuleLinkCreate> T setRuleOpId(String ruleOpId) {
        this.ruleOpId = ruleOpId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRuleOp getFlexiCoreRuleOp() {
        return flexiCoreRuleOp;
    }

    public <T extends RuleLinkCreate> T setFlexiCoreRuleOp(FlexiCoreRuleOp flexiCoreRuleOp) {
        this.flexiCoreRuleOp = flexiCoreRuleOp;
        return (T) this;
    }

    public String getRuleid() {
        return ruleid;
    }

    public <T extends RuleLinkCreate> T setRuleid(String ruleid) {
        this.ruleid = ruleid;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRule getFlexiCoreRule() {
        return flexiCoreRule;
    }

    public <T extends RuleLinkCreate> T setFlexiCoreRule(FlexiCoreRule flexiCoreRule) {
        this.flexiCoreRule = flexiCoreRule;
        return (T) this;
    }
}
