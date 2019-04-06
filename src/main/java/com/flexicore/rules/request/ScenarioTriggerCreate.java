package com.flexicore.rules.request;

public class ScenarioTriggerCreate {
    private String name;
    private String description;
    private String eventCanonicalClassName;

    public String getName() {
        return name;
    }

    public <T extends ScenarioTriggerCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }

    public <T extends ScenarioTriggerCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getEventCanonicalClassName() {
        return eventCanonicalClassName;
    }

    public <T extends ScenarioTriggerCreate> T setEventCanonicalClassName(String eventCanonicalClassName) {
        this.eventCanonicalClassName = eventCanonicalClassName;
        return (T) this;
    }
}
