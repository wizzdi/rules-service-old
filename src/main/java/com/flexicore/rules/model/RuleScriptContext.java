package com.flexicore.rules.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexicore.rules.request.ScenarioTriggerEvent;
import com.flexicore.security.SecurityContext;

import java.util.logging.Logger;

public class RuleScriptContext {
    @JsonIgnore
    private Logger logger;
    private SecurityContext securityContext;
    private ScenarioTriggerEvent<?> scenarioTriggerEvent;
    private static final ObjectMapper objectMapper=new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

    @JsonIgnore
    public Logger getLogger() {
        return logger;
    }

    public <T extends RuleScriptContext> T setLogger(Logger logger) {
        this.logger = logger;
        return (T) this;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public <T extends RuleScriptContext> T setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        return (T) this;
    }

    public ScenarioTriggerEvent<?> getScenarioTriggerEvent() {
        return scenarioTriggerEvent;
    }

    public <T extends RuleScriptContext> T setScenarioTriggerEvent(ScenarioTriggerEvent<?> scenarioTriggerEvent) {
        this.scenarioTriggerEvent = scenarioTriggerEvent;
        return (T) this;
    }

    public String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
