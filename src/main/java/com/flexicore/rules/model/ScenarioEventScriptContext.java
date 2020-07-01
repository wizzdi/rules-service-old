package com.flexicore.rules.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.request.ScenarioTriggerEvent;
import com.flexicore.security.SecurityContext;

import java.util.logging.Logger;

public class ScenarioEventScriptContext {
	@JsonIgnore
	private Logger logger;
	private SecurityContext securityContext;
	private ScenarioEvent scenarioEvent;
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
}
