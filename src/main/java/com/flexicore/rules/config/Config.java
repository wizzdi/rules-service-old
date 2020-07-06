package com.flexicore.rules.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.InitPlugin;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.utils.InheritanceUtils;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@PluginInfo(version = 1, autoInstansiate = true)
@Extension
@Component
public class Config implements InitPlugin {

	private static final AtomicBoolean init = new AtomicBoolean(false);

	@Override
	public void init() {
		if (init.compareAndSet(false, true)) {
			IEventService.addClassForMongoCodec(ScenarioEvent.class);
			InheritanceUtils.registerClass(ScenarioEvent.class, true);

		}
	}
}
