package com.flexicore.rules.response;

public class EvaluateTriggerResponse {
	private boolean result;

	public boolean isResult() {
		return result;
	}

	public <T extends EvaluateTriggerResponse> T setResult(boolean result) {
		this.result = result;
		return (T) this;
	}
}
