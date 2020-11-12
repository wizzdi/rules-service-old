package com.flexicore.rules.events;

import com.flexicore.product.interfaces.IEvent;

import java.util.Set;

public interface IScenarioEvent extends IEvent {


    <T extends ScenarioEvent> T setExecutedActions(
            Set<String> executedActions);

    <T extends ScenarioEvent> T setTriggerId(String triggerId);

    void addToHumanReadableString(String s);

    <T extends ScenarioEvent> T setScenarioHints(
            Set<String> scenarioHints);
}
