package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;

public class RuleUpdate extends RuleCreate {
	private String id;
	@JsonIgnore
	private FlexiCoreRule flexiCoreRule;

	public String getId() {
		return id;
	}

	public <T extends RuleUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRule getFlexiCoreRule() {
		return flexiCoreRule;
	}

	public <T extends RuleUpdate> T setFlexiCoreRule(FlexiCoreRule flexiCoreRule) {
		this.flexiCoreRule = flexiCoreRule;
		return (T) this;
	}
}
