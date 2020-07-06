package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.model.ScenarioTriggerType;

import java.time.OffsetDateTime;

public class ActionReplacementCreate extends BaseclassCreate {


	private String scenarioTriggerId;
	@JsonIgnore
	private ScenarioTrigger scenarioTrigger;
	private String scenarioToActionId;
	@JsonIgnore
	private ScenarioToAction scenarioToAction;
	private String executionTargetPath;
	private String eventSourcePath;


	public String getScenarioTriggerId() {
		return scenarioTriggerId;
	}

	public <T extends ActionReplacementCreate> T setScenarioTriggerId(String scenarioTriggerId) {
		this.scenarioTriggerId = scenarioTriggerId;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioTrigger getScenarioTrigger() {
		return scenarioTrigger;
	}

	public <T extends ActionReplacementCreate> T setScenarioTrigger(ScenarioTrigger scenarioTrigger) {
		this.scenarioTrigger = scenarioTrigger;
		return (T) this;
	}

	public String getScenarioToActionId() {
		return scenarioToActionId;
	}

	public <T extends ActionReplacementCreate> T setScenarioToActionId(String scenarioToActionId) {
		this.scenarioToActionId = scenarioToActionId;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioToAction getScenarioToAction() {
		return scenarioToAction;
	}

	public <T extends ActionReplacementCreate> T setScenarioToAction(ScenarioToAction scenarioToAction) {
		this.scenarioToAction = scenarioToAction;
		return (T) this;
	}

	public String getExecutionTargetPath() {
		return executionTargetPath;
	}

	public <T extends ActionReplacementCreate> T setExecutionTargetPath(String executionTargetPath) {
		this.executionTargetPath = executionTargetPath;
		return (T) this;
	}

	public String getEventSourcePath() {
		return eventSourcePath;
	}

	public <T extends ActionReplacementCreate> T setEventSourcePath(String eventSourcePath) {
		this.eventSourcePath = eventSourcePath;
		return (T) this;
	}
}
