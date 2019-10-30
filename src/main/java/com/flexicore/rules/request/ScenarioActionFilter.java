package com.flexicore.rules.request;

import com.flexicore.model.FilteringInformationHolder;

public class ScenarioActionFilter extends FilteringInformationHolder {


    private ScenarioFilter scenarioFilter;
    private boolean connected;


    public ScenarioFilter getScenarioFilter() {
        return scenarioFilter;
    }

    public <T extends ScenarioActionFilter> T setScenarioFilter(ScenarioFilter scenarioFilter) {
        this.scenarioFilter = scenarioFilter;
        return (T) this;
    }

    public boolean isConnected() {
        return connected;
    }

    public <T extends ScenarioActionFilter> T setConnected(boolean connected) {
        this.connected = connected;
        return (T) this;
    }
}
