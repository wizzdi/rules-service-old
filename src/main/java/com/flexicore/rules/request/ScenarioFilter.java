package com.flexicore.rules.request;

import com.flexicore.model.FilteringInformationHolder;

public class ScenarioFilter extends FilteringInformationHolder {

    private Boolean noLogs;

    public Boolean getNoLogs() {
        return noLogs;
    }

    public <T extends ScenarioFilter> T setNoLogs(Boolean noLogs) {
        this.noLogs = noLogs;
        return (T) this;
    }
}
