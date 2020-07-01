package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioTrigger;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A container for the creation of ScenarioToTrigger object, note that this is not the link itself")
public class ScenarioToTriggerCreate extends BaseclassCreate {

	private String scenarioId;
	private String triggerId;
	@JsonIgnore
	private Scenario scenario;
	@JsonIgnore
	private ScenarioTrigger scenarioTrigger;
	private Boolean enabled;
	private Integer ordinal;
	private Boolean firing;


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

	public Integer getOrdinal() {
		return ordinal;
	}

	public <T extends ScenarioToTriggerCreate> T setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
		return (T) this;
	}

	public Boolean getFiring() {
		return firing;
	}

	public <T extends ScenarioToTriggerCreate> T setFiring(Boolean firing) {
		this.firing = firing;
		return (T) this;
	}
}
