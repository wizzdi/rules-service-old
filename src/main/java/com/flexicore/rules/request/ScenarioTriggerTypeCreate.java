package com.flexicore.rules.request;

import com.flexicore.request.BaseclassCreate;

public class ScenarioTriggerTypeCreate extends BaseclassCreate {

    private String eventCanonicalName;

    public String getEventCanonicalName() {
        return eventCanonicalName;
    }

    public <T extends ScenarioTriggerTypeCreate> T setEventCanonicalName(String eventCanonicalName) {
        this.eventCanonicalName = eventCanonicalName;
        return (T) this;
    }
}
