package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.request.BaseclassCreate;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A DataSource is created in the same way a DynamicExecution is created")
public class DataSourceCreate extends BaseclassCreate {


	private String dynamicExecutionId;
	@JsonIgnore
	private DynamicExecution dynamicExecution;

	@JsonIgnore
	public DynamicExecution getDynamicExecution() {
		return dynamicExecution;
	}

	public <T extends DataSourceCreate> T setDynamicExecution(
			DynamicExecution dynamicExecution) {
		this.dynamicExecution = dynamicExecution;
		return (T) this;
	}


	public String getDynamicExecutionId() {
		return dynamicExecutionId;
	}

	public <T extends DataSourceCreate> T setDynamicExecutionId(
			String dynamicExecutionId) {
		this.dynamicExecutionId = dynamicExecutionId;
		return (T) this;
	}
}
