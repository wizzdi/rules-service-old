package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.repository.ScenarioTriggerRepository;
import com.flexicore.rules.request.ScenarioTriggerCreate;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerUpdate;
import com.flexicore.security.SecurityContext;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioTriggerService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private ScenarioTriggerRepository repository;



    public void validate(ScenarioTriggerFilter scenarioTriggerArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(ScenarioTriggerCreate creationContainer, SecurityContext securityContext) {


    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public ScenarioTrigger createScenarioTrigger(ScenarioTriggerCreate creationContainer, SecurityContext securityContext) {
        ScenarioTrigger scenarioTrigger = createScenarioTriggerNoMerge(creationContainer, securityContext);
        repository.merge(scenarioTrigger);
        return scenarioTrigger;

    }

    public ScenarioTrigger updateScenarioTrigger(ScenarioTriggerUpdate creationContainer, SecurityContext securityContext) {
        ScenarioTrigger scenarioTrigger=creationContainer.getScenarioTrigger();
        if(updateScenarioTriggerNoMerge(scenarioTrigger,creationContainer)){
            repository.merge(scenarioTrigger);

        }
        return scenarioTrigger;

    }

    private ScenarioTrigger createScenarioTriggerNoMerge(ScenarioTriggerCreate creationContainer, SecurityContext securityContext) {
        ScenarioTrigger scenarioTrigger=ScenarioTrigger.s().CreateUnchecked(creationContainer.getName(),securityContext);
        scenarioTrigger.Init();
        updateScenarioTriggerNoMerge(scenarioTrigger,creationContainer);
        return scenarioTrigger;
    }

    private boolean updateScenarioTriggerNoMerge(ScenarioTrigger scenarioTrigger, ScenarioTriggerCreate creationContainer) {
        boolean update=false;
        if(creationContainer.getName()!=null && !creationContainer.getName().equals(scenarioTrigger.getName())){
            scenarioTrigger.setName(creationContainer.getName());
            update=true;
        }

        if(creationContainer.getDescription()!=null && !creationContainer.getDescription().equals(scenarioTrigger.getDescription())){
            scenarioTrigger.setDescription(creationContainer.getDescription());
            update=true;
        }

        if(creationContainer.getEventCanonicalClassName()!=null && !creationContainer.getEventCanonicalClassName().equals(scenarioTrigger.getEventCanonicalClassName())){
            scenarioTrigger.setEventCanonicalClassName(creationContainer.getEventCanonicalClassName());
            update=true;
        }


        return update;

    }

    public PaginationResponse<ScenarioTrigger> getAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
        List<ScenarioTrigger> list=repository.listAllScenarioTriggers(filter,securityContext);
        long count=repository.countAllScenarioTriggers(filter,securityContext);
        return new PaginationResponse<>(list,filter,count);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
