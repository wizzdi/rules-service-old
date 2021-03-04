package com.flexicore.rules.events;

import com.flexicore.product.interfaces.IEvent;

import java.util.Set;

public interface IScenarioEvent extends IEvent {


    <T extends IScenarioEvent> T setExecutedActions(
            Set<String> executedActions);

    <T extends IScenarioEvent> T setTriggerId(String triggerId);

    void addToHumanReadableString(String s);

    <T extends IScenarioEvent> T setScenarioHints(
            Set<String> scenarioHints);
}
