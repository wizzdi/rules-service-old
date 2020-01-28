package com.flexicore.rules.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flexicore.request.ExecuteInvokerRequest;
import com.flexicore.security.SecurityContext;

import java.util.Map;
import java.util.logging.Logger;

public class ActionManagerContext {
    @JsonIgnore
    private Logger logger;
    private SecurityContext securityContext;
    private Scenario scenario;
    private Map<String, ExecuteInvokerRequest> actionMap;
    private static final ObjectMapper objectMapper=new ObjectMapper();

    @JsonIgnore
    public Logger getLogger() {
        return logger;
    }

    public <T extends ActionManagerContext> T setLogger(Logger logger) {
        this.logger = logger;
        return (T) this;
    }

    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public <T extends ActionManagerContext> T setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        return (T) this;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public <T extends ActionManagerContext> T setScenario(Scenario scenario) {
        this.scenario = scenario;
        return (T) this;
    }

    public Map<String, ExecuteInvokerRequest> getActionMap() {
        return actionMap;
    }

    public <T extends ActionManagerContext> T setActionMap(Map<String, ExecuteInvokerRequest> actionMap) {
        this.actionMap = actionMap;
        return (T) this;
    }

    public String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
