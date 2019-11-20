package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FileResource;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "RuleCreate container")
public class RuleCreate {
    private String name;
    private String description;
    private String evaluationScriptId;
    @JsonIgnore
    private FileResource evaluationScript;
    @JsonIgnore
    private FileResource logFileResource;

    public String getName() {
        return name;
    }
    @Schema(description = "Name of the Rule")
    public <T extends RuleCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }
    @Schema(description = "Description of the Rule")
    public <T extends RuleCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getEvaluationScriptId() {
        return evaluationScriptId;
    }
    @Schema(description = "Id of the FileResource of the Script if eny",required = false)
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

    @JsonIgnore
    public FileResource getLogFileResource() {
        return logFileResource;
    }

    public <T extends RuleCreate> T setLogFileResource(FileResource logFileResource) {
        this.logFileResource = logFileResource;
        return (T) this;
    }
}
