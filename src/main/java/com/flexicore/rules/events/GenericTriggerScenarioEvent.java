package com.flexicore.rules.events;

import java.util.Set;

public class GenericTriggerScenarioEvent extends ScenarioEvent {

	private Set<String> relatedBaseclassesIds;
	private String userData;

	public Set<String> getRelatedBaseclassesIds() {
		return relatedBaseclassesIds;
	}

	public <T extends GenericTriggerScenarioEvent> T setRelatedBaseclassesIds(
			Set<String> relatedBaseclassesIds) {
		this.relatedBaseclassesIds = relatedBaseclassesIds;
		return (T) this;
	}

	public String getUserData() {
		return userData;
	}

	public <T extends GenericTriggerScenarioEvent> T setUserData(String userData) {
		this.userData = userData;
		return (T) this;
	}
}
