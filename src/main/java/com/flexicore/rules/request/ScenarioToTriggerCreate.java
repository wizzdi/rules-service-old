package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.model.TriggerManager;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A container for the creation of ScenarioToTrigger object, note that this is not the link itself")
public class ScenarioToTriggerCreate {
	private String name;
	private String description;
	private String scenarioId;
	private String triggerId;
	@JsonIgnore
	private Scenario scenario;
	@JsonIgnore
	private ScenarioTrigger scenarioTrigger;
	private Boolean enabled;
	private String triggerManagerId;
	@JsonIgnore
	private TriggerManager triggerManager;

	public String getName() {
		return name;
	}
	@Schema(description = "The name of the link")
	public <T extends ScenarioToTriggerCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}
	@Schema(description = "The Description of the link")
	public <T extends ScenarioToTriggerCreate> T setDescription(
			String description) {
		this.description = description;
		return (T) this;
	}

	public String getScenarioId() {
		return scenarioId;
	}
	@Schema(description = "A valid system Id of the Scenario to be connected", required = true)
	public <T extends ScenarioToTriggerCreate> T setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
		return (T) this;
	}

	public String getTriggerId() {
		return triggerId;
	}
	@Schema(description = "A valid system Id of the Trigger  to be connected", required = true)
	public <T extends ScenarioToTriggerCreate> T setTriggerId(String triggerId) {
		this.triggerId = triggerId;
		return (T) this;
	}

	@JsonIgnore
	public Scenario getScenario() {
		return scenario;
	}

	public <T extends ScenarioToTriggerCreate> T setScenario(Scenario scenario) {
		this.scenario = scenario;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioTrigger getScenarioTrigger() {
		return scenarioTrigger;
	}

	public <T extends ScenarioToTriggerCreate> T setScenarioTrigger(
			ScenarioTrigger scenarioTrigger) {
		this.scenarioTrigger = scenarioTrigger;
		return (T) this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public <T extends ScenarioToTriggerCreate> T setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public String getTriggerManagerId() {
		return triggerManagerId;
	}

	public <T extends ScenarioToTriggerCreate> T setTriggerManagerId(
			String triggerManagerId) {
		this.triggerManagerId = triggerManagerId;
		return (T) this;
	}

	@JsonIgnore
	public TriggerManager getTriggerManager() {
		return triggerManager;
	}

	public <T extends ScenarioToTriggerCreate> T setTriggerManager(
			TriggerManager triggerManager) {
		this.triggerManager = triggerManager;
		return (T) this;
	}
}
