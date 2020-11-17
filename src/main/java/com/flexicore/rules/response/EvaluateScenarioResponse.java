package com.flexicore.rules.response;

import com.flexicore.request.ExecuteInvokerRequest;
import com.flexicore.rules.request.EvaluateScenarioRequest;

import java.util.Map;

public class EvaluateScenarioResponse {

    private Map<String, ExecuteInvokerRequest> actions;
    private EvaluateScenarioRequest evaluateScenarioRequest;


    public Map<String, ExecuteInvokerRequest> getActions() {
        return actions;
    }

    public <T extends EvaluateScenarioResponse> T setActions(Map<String, ExecuteInvokerRequest> actions) {
        this.actions = actions;
        return (T) this;
    }

    public EvaluateScenarioRequest getEvaluateScenarioRequest() {
        return evaluateScenarioRequest;
    }

    public <T extends EvaluateScenarioResponse> T setEvaluateScenarioRequest(EvaluateScenarioRequest evaluateScenarioRequest) {
        this.evaluateScenarioRequest = evaluateScenarioRequest;
        return (T) this;
    }
}
