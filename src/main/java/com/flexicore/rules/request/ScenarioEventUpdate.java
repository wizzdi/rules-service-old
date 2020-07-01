package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.events.ScenarioEvent;


public class ScenarioEventUpdate extends ScenarioEventCreate {
	private String id;
	@JsonIgnore
	private ScenarioEvent scenarioEvent;

	public String getId() {
		return id;
	}
	public <T extends ScenarioEventUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioEvent getScenarioEvent() {
		return scenarioEvent;
	}

	public <T extends ScenarioEventUpdate> T setScenarioEvent(
			ScenarioEvent scenarioEvent) {
		this.scenarioEvent = scenarioEvent;
		return (T) this;
	}
}
