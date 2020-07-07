package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.rules.model.ActionReplacement;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.repository.ActionReplacementRepository;
import com.flexicore.rules.request.ActionReplacementCreate;
import com.flexicore.rules.request.ActionReplacementFilter;
import com.flexicore.rules.request.ActionReplacementUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.BaseclassNewService;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class ActionReplacementService implements ServicePlugin {

    @PluginInfo(version = 1)
    @Autowired
    private ActionReplacementRepository repository;

    @Autowired
    private BaseclassNewService baseclassNewService;

    public void validate(ActionReplacementFilter actionReplacementArgumentFilter, SecurityContext securityContext) {
        baseclassNewService.validateFilter(actionReplacementArgumentFilter, securityContext);
        Set<String> scenarioToActionIds=actionReplacementArgumentFilter.getScenarioToActionIds();
        Map<String,ScenarioToAction> scenarioToActionMap=scenarioToActionIds.isEmpty()?new HashMap<>():repository.listByIds(ScenarioToAction.class,scenarioToActionIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
        scenarioToActionIds.removeAll(scenarioToActionMap.keySet());
        if(!scenarioToActionIds.isEmpty()){
            throw new BadRequestException("No ScenarioToActions with ids "+scenarioToActionIds);
        }
        actionReplacementArgumentFilter.setScenarioToActions(new ArrayList<>(scenarioToActionMap.values()));

        Set<String> scenarioTriggerIds=actionReplacementArgumentFilter.getScenarioTriggerIds();
        Map<String,ScenarioTrigger> stringScenarioTriggerMap=scenarioTriggerIds.isEmpty()?new HashMap<>():repository.listByIds(ScenarioTrigger.class,scenarioTriggerIds,securityContext).stream().collect(Collectors.toMap(f->f.getId(),f->f));
        scenarioTriggerIds.removeAll(stringScenarioTriggerMap.keySet());
        if(!scenarioTriggerIds.isEmpty()){
            throw new BadRequestException("No ScenarioTrigger with ids "+scenarioTriggerIds);
        }
        actionReplacementArgumentFilter.setScenarioTriggers(new ArrayList<>(stringScenarioTriggerMap.values()));


    }

    public void validate(ActionReplacementCreate creationContainer, SecurityContext securityContext) {
        String scenarioTriggerId = creationContainer.getScenarioTriggerId();
        ScenarioTrigger scenarioTrigger = scenarioTriggerId != null ? getByIdOrNull(scenarioTriggerId, ScenarioTrigger.class, null, securityContext) : null;
        if (scenarioTrigger == null && scenarioTriggerId != null) {
            throw new BadRequestException("No ScenarioTrigger With id " + scenarioTriggerId);
        }
        creationContainer.setScenarioTrigger(scenarioTrigger);

        String scenarioToActionId = creationContainer.getScenarioToActionId();
        ScenarioToAction scenarioToAction = scenarioToActionId != null ? getByIdOrNull(scenarioToActionId, ScenarioToAction.class, null, securityContext) : null;
        if (scenarioToAction == null && scenarioToActionId != null) {
            throw new BadRequestException("No ScenarioToAction With id " + scenarioToActionId);
        }
        creationContainer.setScenarioToAction(scenarioToAction);


    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public ActionReplacement createActionReplacement(ActionReplacementCreate creationContainer, SecurityContext securityContext) {
        ActionReplacement actionReplacement = createActionReplacementNoMerge(creationContainer, securityContext);
        repository.merge(actionReplacement);
        return actionReplacement;

    }

    public ActionReplacement updateActionReplacement(ActionReplacementUpdate creationContainer, SecurityContext securityContext) {
        ActionReplacement actionReplacement = creationContainer.getActionReplacement();
        if (updateActionReplacementNoMerge(actionReplacement, creationContainer)) {
            repository.merge(actionReplacement);

        }
        return actionReplacement;

    }

    private ActionReplacement createActionReplacementNoMerge(ActionReplacementCreate creationContainer, SecurityContext securityContext) {
        ActionReplacement actionReplacement = new ActionReplacement(creationContainer.getName(), securityContext);
        updateActionReplacementNoMerge(actionReplacement, creationContainer);
        return actionReplacement;
    }

    private boolean updateActionReplacementNoMerge(ActionReplacement actionReplacement, ActionReplacementCreate actionReplacementCreate) {
        boolean update = baseclassNewService.updateBaseclassNoMerge(actionReplacementCreate, actionReplacement);
        if (actionReplacementCreate.getEventSourcePath() != null && !actionReplacementCreate.getEventSourcePath().equals(actionReplacement.getEventSourcePath())) {
            actionReplacement.setEventSourcePath(actionReplacementCreate.getEventSourcePath());
            update = true;
        }
        if (actionReplacementCreate.getExecutionTargetPath() != null && !actionReplacementCreate.getExecutionTargetPath().equals(actionReplacement.getExecutionTargetPath())) {
            actionReplacement.setExecutionTargetPath(actionReplacementCreate.getExecutionTargetPath());
            update = true;
        }
        if (actionReplacementCreate.getScenarioToAction() != null && (actionReplacement.getScenarioToAction() == null || !actionReplacementCreate.getScenarioToAction().getId().equals(actionReplacement.getScenarioToAction().getId()))) {
            actionReplacement.setScenarioToAction(actionReplacementCreate.getScenarioToAction());
            update = true;
        }
        if (actionReplacementCreate.getScenarioTrigger() != null && (actionReplacement.getScenarioTrigger() == null || !actionReplacementCreate.getScenarioTrigger().getId().equals(actionReplacement.getScenarioTrigger().getId()))) {
            actionReplacement.setScenarioTrigger(actionReplacementCreate.getScenarioTrigger());
            update = true;
        }
        return update;

    }

    public PaginationResponse<ActionReplacement> getAllActionReplacements(ActionReplacementFilter filter, SecurityContext securityContext) {
        List<ActionReplacement> list = listAllActionReplacements(filter, securityContext);
        long count = repository.countAllActionReplacements(filter, securityContext);
        return new PaginationResponse<>(list, filter, count);
    }

    public List<ActionReplacement> listAllActionReplacements(ActionReplacementFilter filter, SecurityContext securityContext) {
        return repository.listAllActionReplacements(filter, securityContext);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }

}
