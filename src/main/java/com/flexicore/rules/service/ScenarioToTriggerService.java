package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioAction;
import com.flexicore.rules.model.ScenarioToTrigger;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.repository.ScenarioToTriggerRepository;
import com.flexicore.rules.request.ScenarioToTriggerCreate;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.rules.request.ScenarioToTriggerUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioToTriggerService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private ScenarioToTriggerRepository repository;

    @Inject
    private DynamicInvokersService dynamicInvokersService;



    public void validate(ScenarioToTriggerFilter scenarioToTriggerArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(ScenarioToTriggerCreate creationContainer, SecurityContext securityContext) {
        String scenarioId = creationContainer.getScenarioId();
        Scenario scenario = scenarioId != null ? getByIdOrNull(scenarioId,Scenario.class,null,securityContext) : null;
        if (scenario == null && scenarioId != null) {
            throw new BadRequestException("No Scenario with id " + scenarioId);
        }
        creationContainer.setScenario(scenario);

        String triggerId = creationContainer.getTriggerId();
        ScenarioTrigger trigger = triggerId != null ? getByIdOrNull(triggerId,ScenarioTrigger.class,null,securityContext) : null;
        if (trigger == null && triggerId != null) {
            throw new BadRequestException("No ScenarioTrigger with id " + triggerId);
        }
        creationContainer.setScenarioTrigger(trigger);


    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public ScenarioToTrigger createScenarioToTrigger(ScenarioToTriggerCreate creationContainer, SecurityContext securityContext) {
        ScenarioToTrigger scenarioToTrigger = createScenarioToTriggerNoMerge(creationContainer, securityContext);
        repository.merge(scenarioToTrigger);
        return scenarioToTrigger;

    }

    public ScenarioToTrigger updateScenarioToTrigger(ScenarioToTriggerUpdate creationContainer, SecurityContext securityContext) {
        ScenarioToTrigger scenarioToTrigger=creationContainer.getScenarioToTrigger();
        if(updateScenarioToTriggerNoMerge(scenarioToTrigger,creationContainer)){
            repository.merge(scenarioToTrigger);

        }
        return scenarioToTrigger;

    }

    private ScenarioToTrigger createScenarioToTriggerNoMerge(ScenarioToTriggerCreate creationContainer, SecurityContext securityContext) {
        ScenarioToTrigger scenarioToTrigger=ScenarioToTrigger.s().CreateUnchecked(creationContainer.getName(),securityContext);
        scenarioToTrigger.Init();
        updateScenarioToTriggerNoMerge(scenarioToTrigger,creationContainer);
        return scenarioToTrigger;
    }

    private boolean updateScenarioToTriggerNoMerge(ScenarioToTrigger scenarioToTrigger, ScenarioToTriggerCreate creationContainer) {
        boolean update=false;
        if(creationContainer.getName()!=null && !creationContainer.getName().equals(scenarioToTrigger.getName())){
            scenarioToTrigger.setName(creationContainer.getName());
            update=true;
        }

        if(creationContainer.getDescription()!=null && !creationContainer.getDescription().equals(scenarioToTrigger.getDescription())){
            scenarioToTrigger.setDescription(creationContainer.getDescription());
            update=true;
        }
        if(creationContainer.getScenario()!=null && (scenarioToTrigger.getScenario()==null||!creationContainer.getScenario().getId().equals(scenarioToTrigger.getScenario().getId()))){
            scenarioToTrigger.setScenario(creationContainer.getScenario());
            update=true;
        }

        if(creationContainer.getScenarioTrigger()!=null && (scenarioToTrigger.getScenarioTrigger()==null||!creationContainer.getScenarioTrigger().getId().equals(scenarioToTrigger.getScenarioTrigger().getId()))){
            scenarioToTrigger.setScenarioTrigger(creationContainer.getScenarioTrigger());
            update=true;
        }

        if(creationContainer.getEnabled()!=null && creationContainer.getEnabled()!=scenarioToTrigger.isEnabled()){
            scenarioToTrigger.setEnabled(creationContainer.getEnabled());
            update=true;
        }


        return update;

    }

    public PaginationResponse<ScenarioToTrigger> getAllScenarioToTriggers(ScenarioToTriggerFilter filter, SecurityContext securityContext) {
        List<ScenarioToTrigger> list=repository.listAllScenarioToTriggers(filter,securityContext);
        long count=repository.countAllScenarioToTriggers(filter,securityContext);
        return new PaginationResponse<>(list,filter,count);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
