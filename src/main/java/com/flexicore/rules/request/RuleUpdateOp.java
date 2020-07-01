package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRuleOp;

public class RuleUpdateOp extends RuleCreateOp {
	private String id;
	@JsonIgnore
	private FlexiCoreRuleOp flexiCoreRuleOp;

	public String getId() {
		return id;
	}

	public <T extends RuleUpdateOp> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRuleOp getFlexiCoreRuleOp() {
		return flexiCoreRuleOp;
	}

	public <T extends RuleUpdateOp> T setFlexiCoreRuleOp(
			FlexiCoreRuleOp flexiCoreRuleOp) {
		this.flexiCoreRuleOp = flexiCoreRuleOp;
		return (T) this;
	}
}
