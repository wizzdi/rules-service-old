package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.request.CreateDynamicExecution;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A ScenarioAction is created in the same way a DynamicExecution is created")
public class ScenarioActionCreate {

    private String name;
    private String description;
    private String dynamicExecutionId;
    @JsonIgnore
    private DynamicExecution dynamicExecution;

    @JsonIgnore
    public DynamicExecution getDynamicExecution() {
        return dynamicExecution;
    }

    public <T extends ScenarioActionCreate> T setDynamicExecution(DynamicExecution dynamicExecution) {
        this.dynamicExecution = dynamicExecution;
        return (T) this;
    }

    public String getName() {
        return name;
    }

    public <T extends ScenarioActionCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends ScenarioActionCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getDynamicExecutionId() {
        return dynamicExecutionId;
    }

    public <T extends ScenarioActionCreate> T setDynamicExecutionId(String dynamicExecutionId) {
        this.dynamicExecutionId = dynamicExecutionId;
        return (T) this;
    }
}
