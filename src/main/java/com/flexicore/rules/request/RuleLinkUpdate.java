package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRuleLink;

public class RuleLinkUpdate extends RuleLinkCreate {
	private String id;
	@JsonIgnore
	private FlexiCoreRuleLink flexiCoreRuleLink;

	public String getId() {
		return id;
	}

	public <T extends RuleLinkUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public FlexiCoreRuleLink getFlexiCoreRuleLink() {
		return flexiCoreRuleLink;
	}

	public <T extends RuleLinkUpdate> T setFlexiCoreRuleLink(
			FlexiCoreRuleLink flexiCoreRuleLink) {
		this.flexiCoreRuleLink = flexiCoreRuleLink;
		return (T) this;
	}
}
