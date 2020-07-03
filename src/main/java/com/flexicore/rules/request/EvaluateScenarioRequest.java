package com.flexicore.rules.request;

import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.*;

import java.util.List;

public class EvaluateScenarioRequest {
    private Scenario scenario;
    private List<ScenarioTrigger> scenarioTriggers;
    private List<DataSource> dataSources;
    private ScenarioEvent scenarioEvent;

    public Scenario getScenario() {
        return scenario;
    }

    public <T extends EvaluateScenarioRequest> T setScenario(Scenario scenario) {
        this.scenario = scenario;
        return (T) this;
    }

    public List<ScenarioTrigger> getScenarioTriggers() {
        return scenarioTriggers;
    }

    public <T extends EvaluateScenarioRequest> T setScenarioTriggers(List<ScenarioTrigger> scenarioTriggers) {
        this.scenarioTriggers = scenarioTriggers;
        return (T) this;
    }


    public List<DataSource> getDataSources() {
        return dataSources;
    }

    public <T extends EvaluateScenarioRequest> T setDataSources(List<DataSource> dataSources) {
        this.dataSources = dataSources;
        return (T) this;
    }

    public ScenarioEvent getScenarioEvent() {
        return scenarioEvent;
    }

    public <T extends EvaluateScenarioRequest> T setScenarioEvent(ScenarioEvent scenarioEvent) {
        this.scenarioEvent = scenarioEvent;
        return (T) this;
    }
}

