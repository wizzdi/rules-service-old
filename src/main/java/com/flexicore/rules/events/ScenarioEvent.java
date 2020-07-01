package com.flexicore.rules.events;

import com.flexicore.product.model.Equipment;
import com.flexicore.product.model.Event;

import java.util.Set;

public class ScenarioEvent extends Event {

	private String triggerId;
	private Set<String> scenarioHints;
	private Set<String> executedActions;

	public ScenarioEvent() {
		super();
	}

	public ScenarioEvent(Equipment equipment) {
		super(equipment);
	}

	public Set<String> getExecutedActions() {
		return executedActions;
	}

	public <T extends ScenarioEvent> T setExecutedActions(
			Set<String> executedActions) {
		this.executedActions = executedActions;
		return (T) this;
	}

	public String getTriggerId() {
		return triggerId;
	}

	public <T extends ScenarioEvent> T setTriggerId(String triggerId) {
		this.triggerId = triggerId;
		return (T) this;
	}

	public void addToHumanReadableString(String s) {
		String existing = getHumanReadableText();
		setHumanReadableText(existing != null ? (existing + "," + s) : s);
	}

	public Set<String> getScenarioHints() {
		return scenarioHints;
	}

	public <T extends ScenarioEvent> T setScenarioHints(
			Set<String> scenarioHints) {
		this.scenarioHints = scenarioHints;
		return (T) this;
	}

	@Override
	public String toString() {
		return "ScenarioEvent{" + "triggerId='" + triggerId + '\''
				+ ", scenarioHints=" + scenarioHints + ", executedActions="
				+ executedActions + "} " + super.toString();
	}
}
