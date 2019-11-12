package com.flexicore.rules.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.InitPlugin;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.rules.events.ManualFireEvent;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.service.BaseclassService;

import java.util.concurrent.atomic.AtomicBoolean;

@PluginInfo(version = 1,autoInstansiate = true)
public class Config implements InitPlugin {

    private static final AtomicBoolean init=new AtomicBoolean(false);

    @Override
    public void init() {
        if(init.compareAndSet(false,true)){
            IEventService.addClassForMongoCodec(ScenarioEvent.class);
            BaseclassService.registerClass(ScenarioEvent.class,true);
            IEventService.addClassForMongoCodec(ManualFireEvent.class);
            BaseclassService.registerClass(ManualFireEvent.class,true);


        }
    }
}
