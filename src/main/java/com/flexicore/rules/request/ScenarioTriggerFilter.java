package com.flexicore.rules.request;

import com.flexicore.model.FilteringInformationHolder;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Currently this filter adds nothing on top of its super class fields")
public class ScenarioTriggerFilter extends FilteringInformationHolder {

	private ScenarioFilter scenarioFilter;
	private boolean connected;

	public ScenarioFilter getScenarioFilter() {
		return scenarioFilter;
	}

	public <T extends ScenarioTriggerFilter> T setScenarioFilter(
			ScenarioFilter scenarioFilter) {
		this.scenarioFilter = scenarioFilter;
		return (T) this;
	}

	public boolean isConnected() {
		return connected;
	}

	public <T extends ScenarioTriggerFilter> T setConnected(boolean connected) {
		this.connected = connected;
		return (T) this;
	}
}
