package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.ExecutionParametersHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleArgument;
import com.flexicore.rules.model.RuleToArgument;
import com.flexicore.rules.repository.RuleToArgumentHolderRepository;
import com.flexicore.rules.request.RuleToArgumentCreate;
import com.flexicore.rules.request.RuleToArgumentHolderFilter;
import com.flexicore.rules.request.RuleToArgumentUpdate;
import com.flexicore.security.SecurityContext;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.*;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
public class RuleToArgumentService implements ServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private RuleToArgumentHolderRepository repository;

    public void validate(RuleToArgumentHolderFilter rulesToExecutionParametersFilter, SecurityContext securityContext) {
        Set<String> ruleIds=rulesToExecutionParametersFilter.getRulesIds();
        Map<String,FlexiCoreRule> ruleMap=ruleIds.isEmpty()?new HashMap<>():repository.listByIds(FlexiCoreRule.class,ruleIds,securityContext).parallelStream().parallel().collect(Collectors.toMap(f->f.getId(),f->f));
        ruleIds.removeAll(ruleMap.keySet());
        if(!ruleIds.isEmpty()){
            throw new BadRequestException("No Rules with ids "+ruleIds);
        }
        rulesToExecutionParametersFilter.setRules(new ArrayList<>(ruleMap.values()));


    }

    public void validate(RuleToArgumentCreate creationContainer, SecurityContext securityContext) {
        String flexicoreRuleArgumentId = creationContainer.getFlexicoreRuleArgumentId();
        FlexiCoreRuleArgument flexiCoreRuleArgument = flexicoreRuleArgumentId != null ? getByIdOrNull(flexicoreRuleArgumentId,FlexiCoreRuleArgument.class,null,securityContext) : null;
        if (flexiCoreRuleArgument == null && flexicoreRuleArgumentId != null) {
            throw new BadRequestException("No ExecutionParametersHolder with id " + flexicoreRuleArgumentId);
        }
        creationContainer.setFlexiCoreRuleArgument(flexiCoreRuleArgument);

        String flexiCoreRuleId = creationContainer.getFlexiCoreRuleId();
        FlexiCoreRule flexiCoreRule = flexiCoreRuleId != null ? getByIdOrNull(flexiCoreRuleId, FlexiCoreRule.class, null, securityContext) : null;
        if (flexiCoreRule == null && flexiCoreRuleId != null) {
            throw new BadRequestException("No FlexiCoreRule with id " + flexiCoreRuleId);
        }
        creationContainer.setFlexiCoreRule(flexiCoreRule);

    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public RuleToArgument createRulesToExecutionParameter(RuleToArgumentCreate creationContainer, SecurityContext securityContext) {
        RuleToArgument flexiCoreRulesToExecutionParameter = createRulesToExecutionParameterNoMerge(creationContainer, securityContext);
        repository.merge(flexiCoreRulesToExecutionParameter);
        return flexiCoreRulesToExecutionParameter;

    }

    public RuleToArgument updateRulesToExecutionParameter(RuleToArgumentUpdate creationContainer, SecurityContext securityContext) {
        RuleToArgument flexiCoreRulesToExecutionParameter=creationContainer.getRuleToArgument();
        if(updateRulesToExecutionParameterNoMerge(flexiCoreRulesToExecutionParameter,creationContainer)){
            repository.merge(flexiCoreRulesToExecutionParameter);

        }
        return flexiCoreRulesToExecutionParameter;

    }

    private RuleToArgument createRulesToExecutionParameterNoMerge(RuleToArgumentCreate creationContainer, SecurityContext securityContext) {
        RuleToArgument flexiCoreRulesToExecutionParameter=RuleToArgument.s().CreateUnchecked(creationContainer.getName(),securityContext);
        flexiCoreRulesToExecutionParameter.Init();
        updateRulesToExecutionParameterNoMerge(flexiCoreRulesToExecutionParameter,creationContainer);
        return flexiCoreRulesToExecutionParameter;
    }

    private boolean updateRulesToExecutionParameterNoMerge(RuleToArgument flexiCoreRulesToExecutionParameter, RuleToArgumentCreate creationContainer) {
        boolean update=false;
        if(creationContainer.getOrdinal()!=null && creationContainer.getOrdinal()!=flexiCoreRulesToExecutionParameter.getOrdinal()){
            flexiCoreRulesToExecutionParameter.setOrdinal(creationContainer.getOrdinal());
            update=true;
        }
        if(creationContainer.getName()!=null && !creationContainer.getName().equals(flexiCoreRulesToExecutionParameter.getName())){
            flexiCoreRulesToExecutionParameter.setName(creationContainer.getName());
            update=true;
        }

        if(creationContainer.getDescription()!=null && !creationContainer.getDescription().equals(flexiCoreRulesToExecutionParameter.getDescription())){
            flexiCoreRulesToExecutionParameter.setDescription(creationContainer.getDescription());
            update=true;
        }

        if(creationContainer.getFlexiCoreRule()!=null && (flexiCoreRulesToExecutionParameter.getFlexiCoreRule()==null||!creationContainer.getFlexiCoreRule().equals(flexiCoreRulesToExecutionParameter.getFlexiCoreRule()))){
            flexiCoreRulesToExecutionParameter.setFlexiCoreRule(creationContainer.getFlexiCoreRule());
            update=true;
        }

        if(creationContainer.getFlexiCoreRuleArgument()!=null && (flexiCoreRulesToExecutionParameter.getFlexiCoreRuleArgument()==null||!creationContainer.getFlexiCoreRuleArgument().equals(flexiCoreRulesToExecutionParameter.getFlexiCoreRuleArgument()))){
            flexiCoreRulesToExecutionParameter.setFlexiCoreRuleArgument(creationContainer.getFlexiCoreRuleArgument());
            update=true;
        }
        return update;

    }

    public PaginationResponse<RuleToArgument> getAllRulesToExecutionParameters(RuleToArgumentHolderFilter filter, SecurityContext securityContext) {
        List<RuleToArgument> list= listRuleToArgument(filter, securityContext);
        long count=repository.countAllRuleToArguments(filter,securityContext);
        return new PaginationResponse<>(list,filter,count);
    }

    public List<RuleToArgument> listRuleToArgument(RuleToArgumentHolderFilter filter, SecurityContext securityContext) {
        return repository.listAllRuleToArguments(filter,securityContext);
    }

    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
