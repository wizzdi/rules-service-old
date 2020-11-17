package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.request.BaseclassCreate;
import com.flexicore.rules.model.JSFunction;

public class JSFunctionParameterCreate extends BaseclassCreate {


	private String parameterType;
	private Integer ordinal;
	private String jsFunctionId;
	@JsonIgnore
	private JSFunction jsFunction;

	public String getParameterType() {
		return parameterType;
	}

	public <T extends JSFunctionParameterCreate> T setParameterType(String parameterType) {
		this.parameterType = parameterType;
		return (T) this;
	}

	public Integer getOrdinal() {
		return ordinal;
	}

	public <T extends JSFunctionParameterCreate> T setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
		return (T) this;
	}

	public String getJsFunctionId() {
		return jsFunctionId;
	}

	public <T extends JSFunctionParameterCreate> T setJsFunctionId(String jsFunctionId) {
		this.jsFunctionId = jsFunctionId;
		return (T) this;
	}

	@JsonIgnore
	public JSFunction getJsFunction() {
		return jsFunction;
	}

	public <T extends JSFunctionParameterCreate> T setJsFunction(JSFunction jsFunction) {
		this.jsFunction = jsFunction;
		return (T) this;
	}
}
