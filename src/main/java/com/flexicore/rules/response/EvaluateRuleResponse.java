package com.flexicore.rules.response;

public class EvaluateRuleResponse {
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public <T extends EvaluateRuleResponse> T setResult(boolean result) {
		this.result = result;
		return (T) this;
	}
}
