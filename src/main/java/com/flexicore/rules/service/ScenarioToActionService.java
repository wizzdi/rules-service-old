package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioAction;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.repository.ScenarioToActionRepository;
import com.flexicore.rules.request.ScenarioToActionCreate;
import com.flexicore.rules.request.ScenarioToActionFilter;
import com.flexicore.rules.request.ScenarioToActionUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioToActionService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private ScenarioToActionRepository repository;

    @Inject
    private DynamicInvokersService dynamicInvokersService;



    public void validate(ScenarioToActionFilter scenarioToActionArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(ScenarioToActionCreate creationContainer, SecurityContext securityContext) {
        String scenarioId = creationContainer.getScenarioId();
        Scenario scenario = scenarioId != null ? getByIdOrNull(scenarioId,Scenario.class,null,securityContext) : null;
        if (scenario == null && scenarioId != null) {
            throw new BadRequestException("No Scenario with id " + scenarioId);
        }
        creationContainer.setScenario(scenario);

        String actionId = creationContainer.getActionId();
        ScenarioAction action = actionId != null ? getByIdOrNull(actionId,ScenarioAction.class,null,securityContext) : null;
        if (action == null && actionId != null) {
            throw new BadRequestException("No ScenarioAction with id " + actionId);
        }
        creationContainer.setScenarioAction(action);


    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public ScenarioToAction createScenarioToAction(ScenarioToActionCreate creationContainer, SecurityContext securityContext) {
        ScenarioToAction scenarioToAction = createScenarioToActionNoMerge(creationContainer, securityContext);
        repository.merge(scenarioToAction);
        return scenarioToAction;

    }

    public ScenarioToAction updateScenarioToAction(ScenarioToActionUpdate creationContainer, SecurityContext securityContext) {
        ScenarioToAction scenarioToAction=creationContainer.getScenarioToAction();
        if(updateScenarioToActionNoMerge(scenarioToAction,creationContainer)){
            repository.merge(scenarioToAction);

        }
        return scenarioToAction;

    }

    private ScenarioToAction createScenarioToActionNoMerge(ScenarioToActionCreate creationContainer, SecurityContext securityContext) {
        ScenarioToAction scenarioToAction=ScenarioToAction.s().CreateUnchecked(creationContainer.getName(),securityContext);
        scenarioToAction.Init();
        updateScenarioToActionNoMerge(scenarioToAction,creationContainer);
        return scenarioToAction;
    }

    private boolean updateScenarioToActionNoMerge(ScenarioToAction scenarioToAction, ScenarioToActionCreate creationContainer) {
        boolean update=false;
        if(creationContainer.getName()!=null && !creationContainer.getName().equals(scenarioToAction.getName())){
            scenarioToAction.setName(creationContainer.getName());
            update=true;
        }

        if(creationContainer.getDescription()!=null && !creationContainer.getDescription().equals(scenarioToAction.getDescription())){
            scenarioToAction.setDescription(creationContainer.getDescription());
            update=true;
        }
        if(creationContainer.getScenario()!=null && (scenarioToAction.getScenario()==null||!creationContainer.getScenario().getId().equals(scenarioToAction.getScenario().getId()))){
            scenarioToAction.setScenario(creationContainer.getScenario());
            update=true;
        }

        if(creationContainer.getScenarioAction()!=null && (scenarioToAction.getScenarioAction()==null||!creationContainer.getScenarioAction().getId().equals(scenarioToAction.getScenarioAction().getId()))){
            scenarioToAction.setScenarioAction(creationContainer.getScenarioAction());
            update=true;
        }


        return update;

    }

    public PaginationResponse<ScenarioToAction> getAllScenarioToActions(ScenarioToActionFilter filter, SecurityContext securityContext) {
        List<ScenarioToAction> list=repository.listAllScenarioToActions(filter,securityContext);
        long count=repository.countAllScenarioToActions(filter,securityContext);
        return new PaginationResponse<>(list,filter,count);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
