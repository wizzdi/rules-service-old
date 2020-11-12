package com.flexicore.rules.request;

import com.flexicore.model.dynamic.ExecutionContext;
import com.flexicore.product.interfaces.IEvent;
import com.flexicore.rules.events.IScenarioEvent;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.security.SecurityContext;

public class ScenarioTriggerEvent<E extends ScenarioTrigger> implements ExecutionContext {

	private SecurityContext securityContext;
	private E scenarioTrigger;
	private IEvent scenarioEvent;

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public <T extends ScenarioTriggerEvent<E>> T setSecurityContext(
			SecurityContext securityContext) {
		this.securityContext = securityContext;
		return (T) this;
	}

	public E getScenarioTrigger() {
		return scenarioTrigger;
	}

	public <T extends ScenarioTriggerEvent<E>> T setScenarioTrigger(
			E scenarioTrigger) {
		this.scenarioTrigger = scenarioTrigger;
		return (T) this;
	}

	public IEvent getScenarioEvent() {
		return scenarioEvent;
	}

	public <T extends ScenarioTriggerEvent<E>> T setScenarioEvent(
			IEvent scenarioEvent) {
		this.scenarioEvent = scenarioEvent;
		return (T) this;
	}

	@Override
	public String toString() {
		return "ScenarioTriggerEvent{" + "securityContext=" + securityContext
				+ ", scenarioTrigger=" + scenarioTrigger + ", scenarioEvent="
				+ scenarioEvent + '}';
	}
}
