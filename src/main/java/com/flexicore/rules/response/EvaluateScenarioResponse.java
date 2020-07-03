package com.flexicore.rules.response;

import com.flexicore.rules.request.EvaluateScenarioRequest;

public class EvaluateScenarioResponse {

    private boolean result;
    private EvaluateScenarioRequest evaluateScenarioRequest;


    public boolean isResult() {
        return result;
    }

    public <T extends EvaluateScenarioResponse> T setResult(boolean result) {
        this.result = result;
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
