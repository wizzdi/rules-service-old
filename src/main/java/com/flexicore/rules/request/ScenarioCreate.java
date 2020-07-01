package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.rules.model.FlexiCoreRule;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A container class for the creation of a new Scenario")
public class ScenarioCreate {

	private String name;
	private String description;
	private String ruleId;
	private String scenarioHint;
	@JsonIgnore
	private FlexiCoreRule flexiCoreRule;
	private String actionManagerScriptId;
	@JsonIgnore
	private FileResource actionManagerScript;
	@Schema(description = "The name of the Scenario")
	public String getName() {
		return name;
	}
	@JsonIgnore
	private FileResource logFileResource;

	public <T extends ScenarioCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}
	@Schema(description = "The description of the Scenario")
	public String getDescription() {
		return description;
	}

	public <T extends ScenarioCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}
	@Schema(description = "A RuleID, this will be the single RuleID, the top AND,OR,NOT RuleID or empty, can be updated later")
	public String getRuleId() {
		return ruleId;
	}

	public <T extends ScenarioCreate> T setRuleId(String ruleId) {
		this.ruleId = ruleId;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRule getFlexiCoreRule() {
		return flexiCoreRule;
	}

	public <T extends ScenarioCreate> T setFlexiCoreRule(
			FlexiCoreRule flexiCoreRule) {
		this.flexiCoreRule = flexiCoreRule;
		return (T) this;
	}

	public String getScenarioHint() {
		return scenarioHint;
	}

	public <T extends ScenarioCreate> T setScenarioHint(String scenarioHint) {
		this.scenarioHint = scenarioHint;
		return (T) this;
	}

	public String getActionManagerScriptId() {
		return actionManagerScriptId;
	}

	public <T extends ScenarioCreate> T setActionManagerScriptId(
			String actionManagerScriptId) {
		this.actionManagerScriptId = actionManagerScriptId;
		return (T) this;
	}

	@JsonIgnore
	public FileResource getActionManagerScript() {
		return actionManagerScript;
	}

	public <T extends ScenarioCreate> T setActionManagerScript(
			FileResource actionManagerScript) {
		this.actionManagerScript = actionManagerScript;
		return (T) this;
	}

	@JsonIgnore
	public FileResource getLogFileResource() {
		return logFileResource;
	}

	public <T extends ScenarioCreate> T setLogFileResource(
			FileResource logFileResource) {
		this.logFileResource = logFileResource;
		return (T) this;
	}
}
