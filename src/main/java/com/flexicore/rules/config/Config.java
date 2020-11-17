package com.flexicore.rules.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.events.PluginsLoadedEvent;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.rules.events.ScenarioSavableEvent;
import com.flexicore.rules.request.ScenarioSavableEventFilter;
import com.flexicore.service.BaseclassService;
import org.pf4j.Extension;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Extension
@PluginInfo(version = 1)
@Component
public class Config implements ServicePlugin {


    @EventListener
    public void init(PluginsLoadedEvent pluginsLoadedEvent){
        BaseclassService.registerFilterClass(ScenarioSavableEventFilter.class,ScenarioSavableEvent.class);
    }
}
