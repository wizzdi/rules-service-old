package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.DataSource;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Schema(description = "ScenarioToDataSourceFilter directly extends FilteringInformationHolder")
public class ScenarioToDataSourceFilter extends FilteringInformationHolder {

	private Set<String> scenarioIds = new HashSet<>();
	@JsonIgnore
	private List<Scenario> scenarios;
	private Set<String> actionsIds = new HashSet<>();
	@JsonIgnore
	private List<DataSource> dataSources;
	private Boolean enabled;

	@JsonIgnore
	public List<Scenario> getScenarios() {
		return scenarios;
	}

	public <T extends ScenarioToDataSourceFilter> T setScenarios(
			List<Scenario> scenarios) {
		this.scenarios = scenarios;
		return (T) this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public <T extends ScenarioToDataSourceFilter> T setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}

	public Set<String> getScenarioIds() {
		return scenarioIds;
	}

	public <T extends ScenarioToDataSourceFilter> T setScenarioIds(
			Set<String> scenarioIds) {
		this.scenarioIds = scenarioIds;
		return (T) this;
	}

	public Set<String> getActionsIds() {
		return actionsIds;
	}

	public <T extends ScenarioToDataSourceFilter> T setActionsIds(
			Set<String> actionsIds) {
		this.actionsIds = actionsIds;
		return (T) this;
	}

	@JsonIgnore
	public List<DataSource> getDataSources() {
		return dataSources;
	}

	public <T extends ScenarioToDataSourceFilter> T setDataSources(
			List<DataSource> dataSources) {
		this.dataSources = dataSources;
		return (T) this;
	}
}
