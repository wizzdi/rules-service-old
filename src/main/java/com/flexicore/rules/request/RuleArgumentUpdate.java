package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleArgument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RuleArgumentUpdateContainer", description = "built like RuleArgumentCreateContainer plus a field for the id to update")
public class RuleArgumentUpdate extends RuleArgumentCreate {
	private String id;
	@JsonIgnore
	private FlexiCoreRuleArgument flexiCoreRuleArgument;
	@Schema(description = " The ID of an existing RuleArgument")
	public String getId() {
		return id;
	}

	public <T extends RuleArgumentUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRuleArgument getFlexiCoreRuleArgument() {
		return flexiCoreRuleArgument;
	}

	public <T extends RuleArgumentUpdate> T setFlexiCoreRuleArgument(
			FlexiCoreRuleArgument flexiCoreRuleArgument) {
		this.flexiCoreRuleArgument = flexiCoreRuleArgument;
		return (T) this;
	}
}
