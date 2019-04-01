package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import com.flexicore.model.dynamic.ExecutionParametersHolder;

public class RuleArgumentCreate {
    private String name;
    private String description;
    private String methodName;
    private String invokerName;
    private String executionParametersHolderId;
    @JsonIgnore
    private ExecutionParametersHolder executionParametersHolder;

    public String getName() {
        return name;
    }

    public <T extends RuleArgumentCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends RuleArgumentCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getMethodName() {
        return methodName;
    }

    public <T extends RuleArgumentCreate> T setMethodName(String methodName) {
        this.methodName = methodName;
        return (T) this;
    }

    public String getInvokerName() {
        return invokerName;
    }

    public <T extends RuleArgumentCreate> T setInvokerName(String invokerName) {
        this.invokerName = invokerName;
        return (T) this;
    }

    public String getExecutionParametersHolderId() {
        return executionParametersHolderId;
    }

    public <T extends RuleArgumentCreate> T setExecutionParametersHolderId(String executionParametersHolderId) {
        this.executionParametersHolderId = executionParametersHolderId;
        return (T) this;
    }

    @JsonIgnore
    public ExecutionParametersHolder getExecutionParametersHolder() {
        return executionParametersHolder;
    }

    public <T extends RuleArgumentCreate> T setExecutionParametersHolder(ExecutionParametersHolder executionParametersHolder) {
        this.executionParametersHolder = executionParametersHolder;
        return (T) this;
    }
}
