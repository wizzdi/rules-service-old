package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.FlexiCoreRule;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleToArgumentHolderFilter extends FilteringInformationHolder {

    private Set<String> rulesIds=new HashSet<>();
    @JsonIgnore
    private List<FlexiCoreRule> rules;


    public Set<String> getRulesIds() {
        return rulesIds;
    }

    public <T extends RuleToArgumentHolderFilter> T setRulesIds(Set<String> rulesIds) {
        this.rulesIds = rulesIds;
        return (T) this;
    }

    @JsonIgnore
    public List<FlexiCoreRule> getRules() {
        return rules;
    }

    public <T extends RuleToArgumentHolderFilter> T setRules(List<FlexiCoreRule> rules) {
        this.rules = rules;
        return (T) this;
    }
}
