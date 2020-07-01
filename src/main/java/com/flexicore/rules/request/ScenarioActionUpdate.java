package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.ScenarioAction;
import io.swagger.v3.oas.annotations.media.Schema;

public class ScenarioActionUpdate extends ScenarioActionCreate {
	private String id;
	@JsonIgnore
	private ScenarioAction scenarioAction;

	public String getId() {
		return id;
	}
	@Schema(description = "The id of the ScenarioAction to update")
	public <T extends ScenarioActionUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioAction getScenarioAction() {
		return scenarioAction;
	}

	public <T extends ScenarioActionUpdate> T setScenarioAction(
			ScenarioAction scenarioAction) {
		this.scenarioAction = scenarioAction;
		return (T) this;
	}
}
