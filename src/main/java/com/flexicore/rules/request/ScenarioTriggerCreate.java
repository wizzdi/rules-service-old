package com.flexicore.rules.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A container for the creation of ScenarioTrigger")
public class ScenarioTriggerCreate {
    private String name;
    private String description;
    private String eventCanonicalClassName;

    public String getName() {
        return name;
    }
    @Schema(description = "The name of the new trigger")
    public <T extends ScenarioTriggerCreate> T setName(String name) {
        this.name = name;
        return (T) this;
    }

    public String getDescription() {
        return description;
    }
@Schema(description = "The description of the new instance")
    public <T extends ScenarioTriggerCreate> T setDescription(String description) {
        this.description = description;
        return (T) this;
    }

    public String getEventCanonicalClassName() {
        return eventCanonicalClassName;
    }
    @Schema(description = "The canonical class name of the event, for example:" )
    public <T extends ScenarioTriggerCreate> T setEventCanonicalClassName(String eventCanonicalClassName) {
        this.eventCanonicalClassName = eventCanonicalClassName;
        return (T) this;
    }
}
