package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.events.ScenarioSavableEvent;


public class ScenarioSavableEventUpdate extends ScenarioSavableEventCreate {
	private String id;
	@JsonIgnore
	private ScenarioSavableEvent scenarioSavableEvent;

	public String getId() {
		return id;
	}
	public <T extends ScenarioSavableEventUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioSavableEvent getScenarioSavableEvent() {
		return scenarioSavableEvent;
	}

	public <T extends ScenarioSavableEventUpdate> T setScenarioSavableEvent(
			ScenarioSavableEvent scenarioSavableEvent) {
		this.scenarioSavableEvent = scenarioSavableEvent;
		return (T) this;
	}
}
