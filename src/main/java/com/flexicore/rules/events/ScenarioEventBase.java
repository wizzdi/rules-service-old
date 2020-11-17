package com.flexicore.rules.events;

import com.flexicore.security.SecurityContext;

public class ScenarioEventBase implements ScenarioEvent{

    private String id;
    private SecurityContext securityContext;


    @Override
    public String getId() {
        return id;
    }

    public <T extends ScenarioEventBase> T setId(String id) {
        this.id = id;
        return (T) this;
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    public <T extends ScenarioEventBase> T setSecurityContext(SecurityContext securityContext) {
        this.securityContext = securityContext;
        return (T) this;
    }
}
