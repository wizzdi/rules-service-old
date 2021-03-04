package com.flexicore.rules.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flexicore.model.Baseclass;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.request.ScenarioTriggerEvent;
import com.flexicore.rules.service.RulesService;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import com.wizzdi.flexicore.boot.dynamic.invokers.request.ExecuteInvokerRequest;

import java.util.Map;
import java.util.logging.Logger;

public class ActionManagerContext {
    @JsonIgnore
    private Logger logger;
    private SecurityContext securityContext;
    private Scenario scenario;
    private Map<String, ExecuteInvokerRequest> actionMap;
    private ScenarioTriggerEvent scenarioTriggerEvent;
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()).configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private RulesService rulesService;
    private BaseclassNewService baseclassNewService;

    public ActionManagerContext(RulesService rulesService, BaseclassNewService baseclassNewService) {
        this.rulesService = rulesService;
        this.baseclassNewService = baseclassNewService;
    }

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

    public <T extends ActionManagerContext> T setSecurityContext(
            SecurityContext securityContext) {
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

    public <T extends ActionManagerContext> T setActionMap(
            Map<String, ExecuteInvokerRequest> actionMap) {
        this.actionMap = actionMap;
        return (T) this;
    }

    public ScenarioTriggerEvent getScenarioTriggerEvent() {
        return scenarioTriggerEvent;
    }

    public <T extends ActionManagerContext> T setScenarioTriggerEvent(
            ScenarioTriggerEvent scenarioTriggerEvent) {
        this.scenarioTriggerEvent = scenarioTriggerEvent;
        return (T) this;
    }

    public String toJson(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }

    public void setDynamicValue(Baseclass baseclass, String key, Object value) {
        BaseclassCreateDynamic baseclassCreate = new BaseclassCreateDynamic();
        baseclassCreate.set(key, value);
        baseclassNewService.updateBaseclassNoMerge(baseclassCreate, baseclass);
    }

    public <T> T copy(T t) throws JsonProcessingException {
        return (T) objectMapper.readValue(objectMapper.writeValueAsString(t), t.getClass());

    }

    public void merge(Object o) {
        rulesService.merge(o);
    }

    private static class BaseclassCreateDynamic extends BaseclassCreate {
        @Override
        public boolean supportingDynamic() {
            return true;
        }
    }
}
