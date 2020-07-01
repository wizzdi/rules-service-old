package com.flexicore.rules.invokers;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.dynamic.Invoker;
import com.flexicore.interfaces.dynamic.InvokerInfo;
import com.flexicore.interfaces.dynamic.InvokerMethodInfo;
import com.flexicore.rules.model.GenericTrigger;
import com.flexicore.rules.model.GenericTriggerRequest;
import com.flexicore.rules.service.GenericTriggerService;
import com.flexicore.security.SecurityContext;

import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@InvokerInfo(displayName = "Generic Trigger Invoker", description = "Invoker for Generic Trigger")
@Extension
@Component
public class GenericTriggerInvoker implements Invoker {

	@PluginInfo(version = 1)
	@Autowired
	private GenericTriggerService genericTriggerService;

	@InvokerMethodInfo(displayName = "fireGenericTrigger", description = "fire Generic Trigger", relatedClasses = {GenericTrigger.class})
	public void fireGenericTrigger(GenericTriggerRequest genericTriggerRequest,
			SecurityContext securityContext) {
		genericTriggerService.validate(genericTriggerRequest, securityContext);
		genericTriggerService.fireGenericTrigger(genericTriggerRequest,
				securityContext);
	}

	@Override
	public Class<?> getHandlingClass() {
		return GenericTrigger.class;
	}
}
