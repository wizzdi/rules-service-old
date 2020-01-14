package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.rules.model.TriggerManager;
import com.flexicore.rules.repository.TriggerManagerRepository;
import com.flexicore.rules.request.TriggerManagerCreate;
import com.flexicore.rules.request.TriggerManagerFilter;
import com.flexicore.rules.request.TriggerManagerUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
public class TriggerManagerService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private TriggerManagerRepository repository;

    @Inject
    private BaseclassNewService baseclassNewService;



    public void validate(TriggerManagerFilter triggerManagerArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(TriggerManagerCreate creationContainer, SecurityContext securityContext) {
        String scriptId = creationContainer.getScriptId();
        FileResource fileResource = scriptId != null ? getByIdOrNull(scriptId,FileResource.class,null,securityContext) : null;
        if (fileResource == null ) {
            throw new BadRequestException("No fileResource with id " + scriptId);
        }
        creationContainer.setTriggerManagerScript(fileResource);

    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public TriggerManager createTriggerManager(TriggerManagerCreate creationContainer, SecurityContext securityContext) {
        TriggerManager triggerManager = createTriggerManagerNoMerge(creationContainer, securityContext);
        repository.merge(triggerManager);
        return triggerManager;

    }

    public TriggerManager updateTriggerManager(TriggerManagerUpdate creationContainer, SecurityContext securityContext) {
        TriggerManager triggerManager=creationContainer.getTriggerManager();
        if(updateTriggerManagerNoMerge(triggerManager,creationContainer)){
            repository.merge(triggerManager);

        }
        return triggerManager;

    }

    private TriggerManager createTriggerManagerNoMerge(TriggerManagerCreate creationContainer, SecurityContext securityContext) {
        TriggerManager triggerManager=new TriggerManager(creationContainer.getName(),securityContext);
        updateTriggerManagerNoMerge(triggerManager,creationContainer);
        return triggerManager;
    }

    private boolean updateTriggerManagerNoMerge(TriggerManager triggerManager, TriggerManagerCreate creationContainer) {
        boolean update=baseclassNewService.updateBaseclassNoMerge(creationContainer,triggerManager);



        if(creationContainer.getTriggerManagerScript()!=null && (triggerManager.getTriggerManagerScript()==null||!creationContainer.getTriggerManagerScript().getId().equals(triggerManager.getTriggerManagerScript().getId()))){
            triggerManager.setTriggerManagerScript(creationContainer.getTriggerManagerScript());
            update=true;
        }
        return update;

    }

    public PaginationResponse<TriggerManager> getAllTriggerManagers(TriggerManagerFilter filter, SecurityContext securityContext) {
        List<TriggerManager> list=repository.listAllTriggerManagers(filter,securityContext);
        long count=repository.countAllTriggerManagers(filter,securityContext);
        return new PaginationResponse<>(list,filter,count);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
