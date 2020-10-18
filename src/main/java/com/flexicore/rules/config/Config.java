package com.flexicore.rules.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.CrossLoaderResolver;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.InitPlugin;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.product.containers.request.EventFiltering;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.rules.events.GenericTriggerScenarioEvent;
import com.flexicore.rules.events.ManualFireEvent;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.GenericTriggerRequest;
import com.flexicore.utils.InheritanceUtils;

import java.util.concurrent.atomic.AtomicBoolean;
import org.pf4j.Extension;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class Config implements ServicePlugin {

	private static final AtomicBoolean init = new AtomicBoolean(false);

	@Async
	@EventListener
	public void init(PluginsLoadedEvent pluginsLoadedEvent) {
		if (init.compareAndSet(false, true)) {
			IEventService.addClassForMongoCodec(ScenarioEvent.class);
			InheritanceUtils.registerClass(ScenarioEvent.class, true);
			IEventService.addClassForMongoCodec(ManualFireEvent.class);
			IEventService
					.addClassForMongoCodec(GenericTriggerScenarioEvent.class);
			InheritanceUtils.registerClass(ManualFireEvent.class, true);
			InheritanceUtils.registerClass(GenericTriggerScenarioEvent.class,
					true);

		}
	}
}
