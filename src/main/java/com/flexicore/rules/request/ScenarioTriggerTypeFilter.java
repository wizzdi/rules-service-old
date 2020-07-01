package com.flexicore.rules.request;

import com.flexicore.model.FilteringInformationHolder;

import java.util.Set;

public class ScenarioTriggerTypeFilter extends FilteringInformationHolder {

    private Set<String> eventCanonicalNames;


    public Set<String> getEventCanonicalNames() {
        return eventCanonicalNames;
    }

    public <T extends ScenarioTriggerTypeFilter> T setEventCanonicalNames(Set<String> eventCanonicalNames) {
        this.eventCanonicalNames = eventCanonicalNames;
        return (T) this;
    }
}
