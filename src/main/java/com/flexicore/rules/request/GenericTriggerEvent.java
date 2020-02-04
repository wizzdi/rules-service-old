package com.flexicore.rules.request;

import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.GenericTrigger;

import java.util.List;

public class GenericTriggerEvent extends ScenarioTriggerEvent<GenericTrigger> {

    private List<Baseclass> baseclasses;
    private String userData;

    public List<Baseclass> getBaseclasses() {
        return baseclasses;
    }

    public <T extends GenericTriggerEvent> T setBaseclasses(List<Baseclass> baseclasses) {
        this.baseclasses = baseclasses;
        return (T) this;
    }

    public String getUserData() {
        return userData;
    }

    public <T extends GenericTriggerEvent> T setUserData(String userData) {
        this.userData = userData;
        return (T) this;
    }
}
