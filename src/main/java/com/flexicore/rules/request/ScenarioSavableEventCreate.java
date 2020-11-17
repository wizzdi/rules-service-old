package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.flexicore.request.BaseclassNoSQLCreate;

import java.util.HashMap;
import java.util.Map;

public class ScenarioSavableEventCreate extends BaseclassNoSQLCreate {


    private Map<String, Object> more = new HashMap<>();

    public ScenarioSavableEventCreate() {
    }

    public ScenarioSavableEventCreate(ScenarioCreateEvent scenarioCreateEvent) {
        setName(scenarioCreateEvent.getName());
        this.more=scenarioCreateEvent.get();

    }

    @JsonAnyGetter
    public Map<String, Object> get() {
        return more;
    }

    @JsonAnySetter
    public void add(String key, Object value) {
        more.put(key, value);
    }
}
