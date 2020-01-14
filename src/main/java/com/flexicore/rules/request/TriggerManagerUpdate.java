package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.rules.model.TriggerManager;

public class TriggerManagerUpdate extends TriggerManagerCreate{
    private String id;
    @JsonIgnore
    private TriggerManager triggerManager;

    public String getId() {
        return id;
    }

    public <T extends TriggerManagerUpdate> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @JsonIgnore
    public TriggerManager getTriggerManager() {
        return triggerManager;
    }

    public <T extends TriggerManagerUpdate> T setTriggerManager(TriggerManager triggerManager) {
        this.triggerManager = triggerManager;
        return (T) this;
    }
}
