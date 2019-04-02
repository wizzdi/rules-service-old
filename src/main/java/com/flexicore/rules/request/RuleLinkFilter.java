package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.FlexiCoreRuleOp;

import java.util.List;

public class RuleLinkFilter extends FilteringInformationHolder {

    @JsonIgnore
    private List<FlexiCoreRuleOp> flexiCoreRuleOps;

    @JsonIgnore
    public List<FlexiCoreRuleOp> getFlexiCoreRuleOps() {
        return flexiCoreRuleOps;
    }

    public <T extends RuleLinkFilter> T setFlexiCoreRuleOps(List<FlexiCoreRuleOp> flexiCoreRuleOps) {
        this.flexiCoreRuleOps = flexiCoreRuleOps;
        return (T) this;
    }
}
