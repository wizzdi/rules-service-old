package com.flexicore.rules.config;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.CrossLoaderResolver;
import com.flexicore.interfaces.InitPlugin;
import com.flexicore.product.containers.request.EventFiltering;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.rules.events.ManualFireEvent;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.GenericTriggerRequest;
import com.flexicore.utils.InheritanceUtils;

import java.util.concurrent.atomic.AtomicBoolean;

@PluginInfo(version = 1,autoInstansiate = true)
public class Config implements InitPlugin {

    private static final AtomicBoolean init=new AtomicBoolean(false);

    @Override
    public void init() {
        if(init.compareAndSet(false,true)){
            IEventService.addClassForMongoCodec(ScenarioEvent.class);
            InheritanceUtils.registerClass(ScenarioEvent.class,true);
            IEventService.addClassForMongoCodec(ManualFireEvent.class);
            InheritanceUtils.registerClass(ManualFireEvent.class,true);



        }
    }
}
