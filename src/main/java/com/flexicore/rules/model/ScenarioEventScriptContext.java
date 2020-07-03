package com.flexicore.rules.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.security.SecurityContext;

import java.util.List;
import java.util.logging.Logger;

public class ScenarioEventScriptContext {
	@JsonIgnore
	private Logger logger;
	private SecurityContext securityContext;
	private List<ScenarioTrigger> scenarioTriggers;
	private List<DataSource> scenarioToDataSources;
	private ScenarioEvent scenarioEvent;
	private Scenario scenario;
	private static final ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule()).configure(
					DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	@JsonIgnore
	public Logger getLogger() {
		return logger;
	}

	public <T extends ScenarioEventScriptContext> T setLogger(Logger logger) {
		this.logger = logger;
		return (T) this;
	}

	public SecurityContext getSecurityContext() {
		return securityContext;
	}

	public <T extends ScenarioEventScriptContext> T setSecurityContext(
			SecurityContext securityContext) {
		this.securityContext = securityContext;
		return (T) this;
	}

	public ScenarioEvent getScenarioEvent() {
		return scenarioEvent;
	}

	public <T extends ScenarioEventScriptContext> T setScenarioEvent(ScenarioEvent scenarioEvent) {
		this.scenarioEvent = scenarioEvent;
		return (T) this;
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public String toJson(Object o) throws JsonProcessingException {
		return objectMapper.writeValueAsString(o);
	}

	public List<ScenarioTrigger> getScenarioTriggers() {
		return scenarioTriggers;
	}

	public <T extends ScenarioEventScriptContext> T setScenarioTriggers(List<ScenarioTrigger> scenarioTriggers) {
		this.scenarioTriggers = scenarioTriggers;
		return (T) this;
	}

	public List<DataSource> getScenarioToDataSources() {
		return scenarioToDataSources;
	}

	public <T extends ScenarioEventScriptContext> T setScenarioToDataSources(List<DataSource> scenarioToDataSources) {
		this.scenarioToDataSources = scenarioToDataSources;
		return (T) this;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public <T extends ScenarioEventScriptContext> T setScenario(Scenario scenario) {
		this.scenario = scenario;
		return (T) this;
	}
}
