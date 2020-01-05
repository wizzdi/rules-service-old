package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.repository.ScenarioRepository;
import com.flexicore.rules.request.ScenarioCreate;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.ScenarioUpdate;
import com.flexicore.security.SecurityContext;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private ScenarioRepository repository;



    public void validate(ScenarioFilter scenarioArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(ScenarioCreate creationContainer, SecurityContext securityContext) {
        String ruleId = creationContainer.getRuleId();
        FlexiCoreRule executionParametersHolder = ruleId != null ? getByIdOrNull(ruleId,FlexiCoreRule.class,null,securityContext) : null;
        if (executionParametersHolder == null && ruleId != null) {
            throw new BadRequestException("No FlexiCoreRule with id " + ruleId);
        }
        creationContainer.setFlexiCoreRule(executionParametersHolder);

    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public Scenario createScenario(ScenarioCreate creationContainer, SecurityContext securityContext) {
        Scenario scenario = createScenarioNoMerge(creationContainer, securityContext);
        repository.merge(scenario);
        return scenario;

    }

    public Scenario updateScenario(ScenarioUpdate creationContainer, SecurityContext securityContext) {
        Scenario scenario=creationContainer.getScenario();
        if(updateScenarioNoMerge(scenario,creationContainer)){
            repository.merge(scenario);

        }
        return scenario;

    }

    private Scenario createScenarioNoMerge(ScenarioCreate creationContainer, SecurityContext securityContext) {
        Scenario scenario=Scenario.s().CreateUnchecked(creationContainer.getName(),securityContext);
        scenario.Init();
        updateScenarioNoMerge(scenario,creationContainer);
        return scenario;
    }

    private boolean updateScenarioNoMerge(Scenario scenario, ScenarioCreate creationContainer) {
        boolean update=false;
        if(creationContainer.getName()!=null && !creationContainer.getName().equals(scenario.getName())){
            scenario.setName(creationContainer.getName());
            update=true;
        }

        if(creationContainer.getDescription()!=null && !creationContainer.getDescription().equals(scenario.getDescription())){
            scenario.setDescription(creationContainer.getDescription());
            update=true;
        }
        if(creationContainer.getScenarioHint()!=null && !creationContainer.getScenarioHint().equals(scenario.getScenarioHint())){
            scenario.setScenarioHint(creationContainer.getScenarioHint());
            update=true;
        }


        if(creationContainer.getFlexiCoreRule()!=null && (scenario.getFlexiCoreRule()==null||!creationContainer.getFlexiCoreRule().getId().equals(scenario.getFlexiCoreRule().getId()))){
            scenario.setFlexiCoreRule(creationContainer.getFlexiCoreRule());
            update=true;
        }
        return update;

    }

    public PaginationResponse<Scenario> getAllScenarios(ScenarioFilter filter, SecurityContext securityContext) {
        List<Scenario> list=repository.listAllScenarios(filter,securityContext);
        long count=repository.countAllScenarios(filter,securityContext);
        return new PaginationResponse<>(list,filter,count);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
