package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.ScenarioTriggerType;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScenarioTriggerFilter extends FilteringInformationHolder {

    private Set<String> scenarioTriggerTypeIds=new HashSet<>();
    @JsonIgnore
    private List<ScenarioTriggerType> scenarioTriggerType;
    private OffsetDateTime validFrom;
    private OffsetDateTime validTill;
    private Set<String> eventCanonicalNames=new HashSet<>();

    public Set<String> getScenarioTriggerTypeIds() {
        return scenarioTriggerTypeIds;
    }

    public <T extends ScenarioTriggerFilter> T setScenarioTriggerTypeIds(Set<String> scenarioTriggerTypeIds) {
        this.scenarioTriggerTypeIds = scenarioTriggerTypeIds;
        return (T) this;
    }

    public OffsetDateTime getValidFrom() {
        return validFrom;
    }

    public <T extends ScenarioTriggerFilter> T setValidFrom(OffsetDateTime validFrom) {
        this.validFrom = validFrom;
        return (T) this;
    }

    public OffsetDateTime getValidTill() {
        return validTill;
    }

    public <T extends ScenarioTriggerFilter> T setValidTill(OffsetDateTime validTill) {
        this.validTill = validTill;
        return (T) this;
    }

    @JsonIgnore
    public List<ScenarioTriggerType> getScenarioTriggerType() {
        return scenarioTriggerType;
    }

    public <T extends ScenarioTriggerFilter> T setScenarioTriggerType(List<ScenarioTriggerType> scenarioTriggerType) {
        this.scenarioTriggerType = scenarioTriggerType;
        return (T) this;
    }

    public Set<String> getEventCanonicalNames() {
        return eventCanonicalNames;
    }

    public <T extends ScenarioTriggerFilter> T setEventCanonicalNames(Set<String> eventCanonicalNames) {
        this.eventCanonicalNames = eventCanonicalNames;
        return (T) this;
    }
}
