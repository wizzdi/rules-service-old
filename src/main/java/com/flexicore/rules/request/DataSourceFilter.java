package com.flexicore.rules.request;

import com.flexicore.model.FilteringInformationHolder;

public class DataSourceFilter extends FilteringInformationHolder {

	private ScenarioFilter scenarioFilter;
	private boolean connected;

	public ScenarioFilter getScenarioFilter() {
		return scenarioFilter;
	}

	public <T extends DataSourceFilter> T setScenarioFilter(
			ScenarioFilter scenarioFilter) {
		this.scenarioFilter = scenarioFilter;
		return (T) this;
	}

	public boolean isConnected() {
		return connected;
	}

	public <T extends DataSourceFilter> T setConnected(boolean connected) {
		this.connected = connected;
		return (T) this;
	}
}
