package com.flexicore.rules.request;

import com.flexicore.model.FilteringInformationHolder;

public class RuleArgumentFilter extends FilteringInformationHolder {



    private RulesFilter rulesFilter;
    private boolean connected;

    public RulesFilter getRulesFilter() {
        return rulesFilter;
    }

    public <T extends RuleArgumentFilter> T setRulesFilter(RulesFilter rulesFilter) {
        this.rulesFilter = rulesFilter;
        return (T) this;
    }

    public boolean isConnected() {
        return connected;
    }

    public <T extends RuleArgumentFilter> T setConnected(boolean connected) {
        this.connected = connected;
        return (T) this;
    }
}
