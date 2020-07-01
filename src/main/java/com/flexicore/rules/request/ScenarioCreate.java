package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.request.BaseclassCreate;

public class ScenarioCreate extends BaseclassCreate {
;
	private String scenarioHint;
	@JsonIgnore
	private FileResource logFileResource;
	@JsonIgnore
	private FileResource evaluatingJSCode;
	private String evaluatingJSCodeId;

	public String getScenarioHint() {
		return scenarioHint;
	}

	public <T extends ScenarioCreate> T setScenarioHint(String scenarioHint) {
		this.scenarioHint = scenarioHint;
		return (T) this;
	}

	public FileResource getLogFileResource() {
		return logFileResource;
	}

	public <T extends ScenarioCreate> T setLogFileResource(FileResource logFileResource) {
		this.logFileResource = logFileResource;
		return (T) this;
	}


	public FileResource getEvaluatingJSCode() {
		return evaluatingJSCode;
	}

	public <T extends ScenarioCreate> T setEvaluatingJSCode(FileResource evaluatingJSCode) {
		this.evaluatingJSCode = evaluatingJSCode;
		return (T) this;
	}

	public String getEvaluatingJSCodeId() {
		return evaluatingJSCodeId;
	}

	public <T extends ScenarioCreate> T setEvaluatingJSCodeId(String evaluatingJSCodeId) {
		this.evaluatingJSCodeId = evaluatingJSCodeId;
		return (T) this;
	}
}
