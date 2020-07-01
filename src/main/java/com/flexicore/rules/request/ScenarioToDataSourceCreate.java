package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.DataSource;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Container used for the creation of ScenarioToActionCreate a ScenarioToActionCreate creates Many To Many Link between an Action and Scenario"
		+ "This means that DataSource can be reused across multiple Scenarios, A Scenario can fire multiple DataSources")
public class ScenarioToDataSourceCreate extends BaseclassCreate {
	private String scenarioId;
	private String actionId;
	@JsonIgnore
	private Scenario scenario;
	@JsonIgnore
	private DataSource dataSource;
	private Boolean enabled;


	public String getScenarioId() {
		return scenarioId;
	}
	@Schema(description = "A valid system ID of the Scenario", required = false)
	public <T extends ScenarioToDataSourceCreate> T setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
		return (T) this;
	}

	public String getActionId() {
		return actionId;
	}
	@Schema(description = "A valid system ID of the Action", required = false)
	public <T extends ScenarioToDataSourceCreate> T setActionId(String actionId) {
		this.actionId = actionId;
		return (T) this;
	}

	@JsonIgnore
	public Scenario getScenario() {
		return scenario;
	}

	public <T extends ScenarioToDataSourceCreate> T setScenario(Scenario scenario) {
		this.scenario = scenario;
		return (T) this;
	}

	@JsonIgnore
	public DataSource getDataSource() {
		return dataSource;
	}

	public <T extends ScenarioToDataSourceCreate> T setDataSource(
			DataSource dataSource) {
		this.dataSource = dataSource;
		return (T) this;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public <T extends ScenarioToDataSourceCreate> T setEnabled(Boolean enabled) {
		this.enabled = enabled;
		return (T) this;
	}
}
