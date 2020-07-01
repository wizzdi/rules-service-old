package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioTrigger;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "Currently extends FilteringInformationHolder with no additional fields ")
public class ScenarioToTriggerFilter extends FilteringInformationHolder {

	private Set<String> scenarioTriggerIds = new HashSet<>();
	@JsonIgnore
	private List<ScenarioTrigger> scenarioTriggers;
	private Set<String> scenarioIds = new HashSet<>();
	@JsonIgnore
	private List<Scenario> scenarios;

	private Boolean enabled;
	private Boolean nonDeletedScenarios;

	@JsonIgnore
	public List<ScenarioTrigger> getScenarioTriggers() {
		return scenarioTriggers;
	}

	public <T extends ScenarioToTriggerFilter> T setScenarioTriggers(
			List<ScenarioTrigger> scenarioTriggers) {
		this.scenarioTriggers = scenarioTriggers;
		return (T) this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public <T extends ScenarioToTriggerFilter> T setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public Set<String> getScenarioTriggerIds() {
		return scenarioTriggerIds;
	}

	public <T extends ScenarioToTriggerFilter> T setScenarioTriggerIds(
			Set<String> scenarioTriggerIds) {
		this.scenarioTriggerIds = scenarioTriggerIds;
		return (T) this;
	}

	public Set<String> getScenarioIds() {
		return scenarioIds;
	}

	public <T extends ScenarioToTriggerFilter> T setScenarioIds(
			Set<String> scenarioIds) {
		this.scenarioIds = scenarioIds;
		return (T) this;
	}

	@JsonIgnore
	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public <T extends ScenarioToTriggerFilter> T setScenarios(
			List<Scenario> scenarios) {
		this.scenarios = scenarios;
		return (T) this;
	}

	@JsonIgnore
	public Boolean getNonDeletedScenarios() {
		return nonDeletedScenarios;
	}

	public <T extends ScenarioToTriggerFilter> T setNonDeletedScenarios(
			Boolean nonDeletedScenarios) {
		this.nonDeletedScenarios = nonDeletedScenarios;
		return (T) this;
	}
}
