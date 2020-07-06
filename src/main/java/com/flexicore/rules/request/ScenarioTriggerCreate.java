package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.model.ScenarioTriggerType;

import java.time.OffsetDateTime;

public class ScenarioTriggerCreate extends BaseclassCreate {


	@JsonIgnore
	private OffsetDateTime activeTill;
	private OffsetDateTime validFrom;
	private OffsetDateTime validTill;
	private String scenarioTriggerTypeId;
	private ScenarioTriggerType scenarioTriggerType;
	private String evaluatingJSCodeId;
	@JsonIgnore
	private FileResource evaluatingJSCode;
	private Long activeMs;

	public OffsetDateTime getActiveTill() {
		return activeTill;
	}

	public <T extends ScenarioTriggerCreate> T setActiveTill(OffsetDateTime activeTill) {
		this.activeTill = activeTill;
		return (T) this;
	}

	public OffsetDateTime getValidFrom() {
		return validFrom;
	}

	public <T extends ScenarioTriggerCreate> T setValidFrom(OffsetDateTime validFrom) {
		this.validFrom = validFrom;
		return (T) this;
	}

	public OffsetDateTime getValidTill() {
		return validTill;
	}

	public <T extends ScenarioTriggerCreate> T setValidTill(OffsetDateTime validTill) {
		this.validTill = validTill;
		return (T) this;
	}

	public String getScenarioTriggerTypeId() {
		return scenarioTriggerTypeId;
	}

	public <T extends ScenarioTriggerCreate> T setScenarioTriggerTypeId(String scenarioTriggerTypeId) {
		this.scenarioTriggerTypeId = scenarioTriggerTypeId;
		return (T) this;
	}

	@JsonIgnore
	public ScenarioTriggerType getScenarioTriggerType() {
		return scenarioTriggerType;
	}

	public <T extends ScenarioTriggerCreate> T setScenarioTriggerType(ScenarioTriggerType scenarioTriggerType) {
		this.scenarioTriggerType = scenarioTriggerType;
		return (T) this;
	}

	public String getEvaluatingJSCodeId() {
		return evaluatingJSCodeId;
	}

	public <T extends ScenarioTriggerCreate> T setEvaluatingJSCodeId(String evaluatingJSCodeId) {
		this.evaluatingJSCodeId = evaluatingJSCodeId;
		return (T) this;
	}

	public FileResource getEvaluatingJSCode() {
		return evaluatingJSCode;
	}

	public <T extends ScenarioTriggerCreate> T setEvaluatingJSCode(FileResource evaluatingJSCode) {
		this.evaluatingJSCode = evaluatingJSCode;
		return (T) this;
	}

	public Long getActiveMs() {
		return activeMs;
	}

	public <T extends ScenarioTriggerCreate> T setActiveMs(Long activeMs) {
		this.activeMs = activeMs;
		return (T) this;
	}
}
