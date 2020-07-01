package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleArgument;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "a container for creating a link between Rule and Argument")
public class RuleToArgumentCreate {
	private String name;
	private String description;
	private Integer ordinal;
	private String flexiCoreRuleId;
	@JsonIgnore
	private FlexiCoreRule flexiCoreRule;
	private String flexicoreRuleArgumentId;
	@JsonIgnore
	private FlexiCoreRuleArgument flexiCoreRuleArgument;
	@Schema(description = "Name of the new link")
	public String getName() {
		return name;
	}

	public <T extends RuleToArgumentCreate> T setName(String name) {
		this.name = name;
		return (T) this;
	}

	public String getDescription() {
		return description;
	}

	public <T extends RuleToArgumentCreate> T setDescription(String description) {
		this.description = description;
		return (T) this;
	}
	@Schema(description = "The RuleId to link with", required = true)
	public String getFlexiCoreRuleId() {
		return flexiCoreRuleId;
	}

	public <T extends RuleToArgumentCreate> T setFlexiCoreRuleId(
			String flexiCoreRuleId) {
		this.flexiCoreRuleId = flexiCoreRuleId;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRule getFlexiCoreRule() {
		return flexiCoreRule;
	}

	public <T extends RuleToArgumentCreate> T setFlexiCoreRule(
			FlexiCoreRule flexiCoreRule) {
		this.flexiCoreRule = flexiCoreRule;
		return (T) this;
	}
	@Schema(description = "The RuleArgument Id to link with", required = true)
	public String getFlexicoreRuleArgumentId() {
		return flexicoreRuleArgumentId;
	}

	public <T extends RuleToArgumentCreate> T setFlexicoreRuleArgumentId(
			String flexicoreRuleArgumentId) {
		this.flexicoreRuleArgumentId = flexicoreRuleArgumentId;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRuleArgument getFlexiCoreRuleArgument() {
		return flexiCoreRuleArgument;
	}

	public <T extends RuleToArgumentCreate> T setFlexiCoreRuleArgument(
			FlexiCoreRuleArgument flexiCoreRuleArgument) {
		this.flexiCoreRuleArgument = flexiCoreRuleArgument;
		return (T) this;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public <T extends RuleToArgumentCreate> T setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
		return (T) this;
	}
}
