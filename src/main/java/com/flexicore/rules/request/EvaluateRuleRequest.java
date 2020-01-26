package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.Scenario;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EvaluateRuleRequest",description = "defines a rule to be evaluated, uses previously retrieved RuleId ")
public class EvaluateRuleRequest {
    private String ruleId;
    @JsonIgnore
    private FlexiCoreRule rule;
    @JsonIgnore
    private ScenarioTriggerEvent<?> scenarioTriggerEvent;
    @JsonIgnore
    private Scenario scenario;
    private String scenarioId;

    public String getRuleId() {
        return ruleId;
    }

    public <T extends EvaluateRuleRequest> T setRuleId(String ruleId) {
        this.ruleId = ruleId;
        return (T) this;
    }

    @JsonIgnore
    public FlexiCoreRule getRule() {
        return rule;
    }

    public <T extends EvaluateRuleRequest> T setRule(FlexiCoreRule rule) {
        this.rule = rule;
        return (T) this;
    }

    @JsonIgnore
    public ScenarioTriggerEvent<?> getScenarioTriggerEvent() {
        return scenarioTriggerEvent;
    }

    public <T extends EvaluateRuleRequest> T setScenarioTriggerEvent(ScenarioTriggerEvent<?> scenarioTriggerEvent) {
        this.scenarioTriggerEvent = scenarioTriggerEvent;
        return (T) this;
    }

    @JsonIgnore
    public Scenario getScenario() {
        return scenario;
    }

    public <T extends EvaluateRuleRequest> T setScenario(Scenario scenario) {
        this.scenario = scenario;
        return (T) this;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public <T extends EvaluateRuleRequest> T setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
        return (T) this;
    }
}
