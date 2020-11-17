package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.model.ScenarioTriggerType;

import java.time.OffsetDateTime;

public class JSFunctionCreate extends BaseclassCreate {


	private String methodName;
	private String returnType;
	private String evaluatingJSCodeId;
	@JsonIgnore
	private FileResource evaluatingJSCode;


	public String getEvaluatingJSCodeId() {
		return evaluatingJSCodeId;
	}

	public <T extends JSFunctionCreate> T setEvaluatingJSCodeId(String evaluatingJSCodeId) {
		this.evaluatingJSCodeId = evaluatingJSCodeId;
		return (T) this;
	}

	public FileResource getEvaluatingJSCode() {
		return evaluatingJSCode;
	}

	public <T extends JSFunctionCreate> T setEvaluatingJSCode(FileResource evaluatingJSCode) {
		this.evaluatingJSCode = evaluatingJSCode;
		return (T) this;
	}

	public String getMethodName() {
		return methodName;
	}

	public <T extends JSFunctionCreate> T setMethodName(String methodName) {
		this.methodName = methodName;
		return (T) this;
	}

	public String getReturnType() {
		return returnType;
	}

	public <T extends JSFunctionCreate> T setReturnType(String returnType) {
		this.returnType = returnType;
		return (T) this;
	}
}
