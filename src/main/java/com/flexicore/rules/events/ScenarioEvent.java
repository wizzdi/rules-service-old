package com.flexicore.rules.events;


import com.flexicore.security.SecurityContext;

public interface ScenarioEvent {

    String getId() ;
    SecurityContext getSecurityContext();


}
