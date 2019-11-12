package com.flexicore.rules.events;

import com.flexicore.product.model.Equipment;

public class ManualFireEvent extends ScenarioEvent {
    public ManualFireEvent() {
    }

    public ManualFireEvent(Equipment equipment) {
        super(equipment);
    }

    private String firingUserId;

    public String getFiringUserId() {
        return firingUserId;
    }

    public <T extends ManualFireEvent> T setFiringUserId(String firingUserId) {
        this.firingUserId = firingUserId;
        return (T) this;
    }
}
