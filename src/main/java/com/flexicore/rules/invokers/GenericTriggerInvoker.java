package com.flexicore.rules.invokers;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.dynamic.Invoker;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.rules.model.GenericTrigger;
import com.flexicore.rules.model.GenericTriggerRequest;
import com.flexicore.rules.service.GenericTriggerService;
import com.flexicore.security.SecurityContext;

import javax.inject.Inject;

@PluginInfo(version = 1)
@InvokerInfo(displayName = "Generic Trigger Invoker", description = "Invoker for Generic Trigger")
public class GenericTriggerInvoker implements Invoker {

    @Inject
    @PluginInfo(version = 1)
    private GenericTriggerService genericTriggerService;

    @InvokerMethodInfo(displayName = "fireGenericTrigger",description = "fire Generic Trigger",relatedClasses = {GenericTrigger.class})
    public void fireGenericTrigger(GenericTriggerRequest genericTriggerRequest, SecurityContext securityContext) {
        genericTriggerService.validate(genericTriggerRequest,securityContext);
        genericTriggerService.fireGenericTrigger(genericTriggerRequest, securityContext);
    }



    @Override
    public Class<?> getHandlingClass() {
        return GenericTrigger.class;
    }
}
