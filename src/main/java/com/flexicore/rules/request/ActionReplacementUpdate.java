package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.ActionReplacement;
import io.swagger.v3.oas.annotations.media.Schema;

public class ActionReplacementUpdate extends ActionReplacementCreate {
	private String id;
	@JsonIgnore
	private ActionReplacement actionReplacement;

	public String getId() {
		return id;
	}
	@Schema(description = "The id of the ActionReplacement to update")
	public <T extends ActionReplacementUpdate> T setId(String id) {
		this.id = id;
		return (T) this;
	}

	@JsonIgnore
	public ActionReplacement getActionReplacement() {
		return actionReplacement;
	}

	public <T extends ActionReplacementUpdate> T setActionReplacement(
			ActionReplacement actionReplacement) {
		this.actionReplacement = actionReplacement;
		return (T) this;
	}
}
