package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.FlexiCoreRuleOp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleLinkFilter extends FilteringInformationHolder {

    @JsonIgnore
    private List<FlexiCoreRuleOp> flexiCoreRuleOps;
    private Set<String> ruleOpsIds=new HashSet<>();

    @JsonIgnore
    public List<FlexiCoreRuleOp> getFlexiCoreRuleOps() {
        return flexiCoreRuleOps;
    }

    public <T extends RuleLinkFilter> T setFlexiCoreRuleOps(List<FlexiCoreRuleOp> flexiCoreRuleOps) {
        this.flexiCoreRuleOps = flexiCoreRuleOps;
        return (T) this;
    }

    public Set<String> getRuleOpsIds() {
        return ruleOpsIds;
    }

    public <T extends RuleLinkFilter> T setRuleOpsIds(Set<String> ruleOpsIds) {
        this.ruleOpsIds = ruleOpsIds;
        return (T) this;
    }
}
