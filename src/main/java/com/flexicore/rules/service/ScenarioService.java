package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.repository.ScenarioRepository;
import com.flexicore.rules.request.ClearLogRequest;
import com.flexicore.rules.request.ScenarioCreate;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.ScenarioUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.FileResourceService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@PluginInfo(version = 1)
public class ScenarioService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private ScenarioRepository repository;

    @Inject
    private FileResourceService fileResourceService;

    @Inject
    private Logger logger;



    public void validate(ScenarioFilter scenarioArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(ScenarioCreate creationContainer, SecurityContext securityContext) {
        String ruleId = creationContainer.getRuleId();
        FlexiCoreRule executionParametersHolder = ruleId != null ? getByIdOrNull(ruleId,FlexiCoreRule.class,null,securityContext) : null;
        if (executionParametersHolder == null && ruleId != null) {
            throw new BadRequestException("No FlexiCoreRule with id " + ruleId);
        }
        creationContainer.setFlexiCoreRule(executionParametersHolder);


        String actionManagerScriptId = creationContainer.getActionManagerScriptId();
        FileResource actionManagerScript = actionManagerScriptId != null ? getByIdOrNull(actionManagerScriptId, FileResource.class,null,securityContext) : null;
        if (actionManagerScript == null && actionManagerScriptId != null) {
            throw new BadRequestException("No FileResource with id " + actionManagerScriptId);
        }
        creationContainer.setActionManagerScript(actionManagerScript);

    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public Scenario createScenario(ScenarioCreate creationContainer, SecurityContext securityContext) {
        List<Object> toMerge=new ArrayList<>();
        File log = new File(FileResourceService.generateNewPathForFileResource(creationContainer.getName(), securityContext.getUser()) + ".log");
        FileResource fileResource=fileResourceService.createDontPersist(log.getAbsolutePath(),securityContext);
        toMerge.add(fileResource);
        creationContainer.setLogFileResource(fileResource);
        Scenario scenario = createScenarioNoMerge(creationContainer, securityContext);
        toMerge.add(scenario);

        repository.massMerge(toMerge);
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
        Scenario scenario=new Scenario(creationContainer.getName(),securityContext);
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

        if(creationContainer.getActionManagerScript()!=null && (scenario.getActionManagerScript()==null||!creationContainer.getActionManagerScript().getId().equals(scenario.getActionManagerScript().getId()))){
            scenario.setActionManagerScript(creationContainer.getActionManagerScript());
            update=true;
        }
        if (creationContainer.getLogFileResource() != null && (scenario.getLogFileResource() == null || !creationContainer.getLogFileResource().equals(scenario.getLogFileResource()))) {
            scenario.setLogFileResource(creationContainer.getLogFileResource());
            update = true;
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

    public void validate(ClearLogRequest clearLogRequest, SecurityContext securityContext) {
        Scenario scenario = clearLogRequest.getScenarioId() != null ? getByIdOrNull(clearLogRequest.getScenarioId(), Scenario.class, null, securityContext) : null;
        if (scenario == null) {
            throw new BadRequestException("No Scenario with id " + clearLogRequest.getScenarioId());
        }
        clearLogRequest.setScenario(scenario);
    }

    public void clearLog(ClearLogRequest creationContainer, SecurityContext securityContext) {
        Scenario scenario = creationContainer.getScenario();
        if(scenario.getLogFileResource()!=null){
            File file=new File(scenario.getLogFileResource().getFullPath());
            try (FileChannel outChan = new FileOutputStream(file, true).getChannel()) {
                outChan.truncate(0);
            }
            catch (Exception e){
                logger.log(Level.SEVERE,"failed clearing log file",e);
            }
        }


    }
}
