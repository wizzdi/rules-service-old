package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.request.BaseclassCreate;

public class TriggerManagerCreate extends BaseclassCreate {

	private String scriptId;
	@JsonIgnore
	private FileResource triggerManagerScript;

	public String getScriptId() {
		return scriptId;
	}

	public <T extends TriggerManagerCreate> T setScriptId(String scriptId) {
		this.scriptId = scriptId;
		return (T) this;
	}

	@JsonIgnore
	public FileResource getTriggerManagerScript() {
		return triggerManagerScript;
	}

	public <T extends TriggerManagerCreate> T setTriggerManagerScript(
			FileResource triggerManagerScript) {
		this.triggerManagerScript = triggerManagerScript;
		return (T) this;
	}
}
