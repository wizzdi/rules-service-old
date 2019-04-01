package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;

public class RuleCreate {
    private String name;
    private String description;
    private String evaluationScriptId;
    @JsonIgnore
    private FileResource evaluationScript;

    public String getName() {
        return name;
    }

    public <T extends RuleCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends RuleCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getEvaluationScriptId() {
        return evaluationScriptId;
    }

    public <T extends RuleCreate> T setEvaluationScriptId(String evaluationScriptId) {
        this.evaluationScriptId = evaluationScriptId;
        return (T) this;
    }

    @JsonIgnore
    public FileResource getEvaluationScript() {
        return evaluationScript;
    }

    public <T extends RuleCreate> T setEvaluationScript(FileResource evaluationScript) {
        this.evaluationScript = evaluationScript;
        return (T) this;
    }
}
