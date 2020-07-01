package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.ScenarioTrigger;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A container for updating a ScenarioTrigger, it extends the ScenarioTriggerCreate container so all fields apart from "
		+ "Id can be set on the Super class fields.")
public class ScenarioTriggerUpdate extends ScenarioTriggerCreate {
	private String id;
	@JsonIgnore
	private ScenarioTrigger scenarioTrigger;

	public String getId() {
		return id;
	}
	@Schema(description = "The Id of an existing trigger, normally obtained by the client in a getAllScenarioTrigger API call ")
	public <T extends ScenarioTriggerUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioTrigger getScenarioTrigger() {
		return scenarioTrigger;
	}

	public <T extends ScenarioTriggerUpdate> T setScenarioTrigger(
			ScenarioTrigger scenarioTrigger) {
		this.scenarioTrigger = scenarioTrigger;
		return (T) this;
	}
}
