package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.dynamic.ExecutionParametersHolder;

import com.flexicore.rules.model.FlexiCoreRuleArgument;
import com.flexicore.rules.repository.RuleArgumentRepository;
import com.flexicore.rules.request.RuleArgumentCreate;
import com.flexicore.rules.request.RuleArgumentFilter;
import com.flexicore.rules.request.RuleArgumentUpdate;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.util.List;

@PluginInfo(version = 1)
public class RuleArgumentService implements ServicePlugin {


    @Inject
    @PluginInfo(version = 1)
    private RuleArgumentRepository repository;

    @Inject
    private DynamicInvokersService dynamicInvokersService;


    public void validate(RuleArgumentFilter ruleArgumentArgumentFilter, SecurityContext securityContext) {


    }

    public void validate(RuleArgumentCreate creationContainer, SecurityContext securityContext) {
        if(creationContainer.getServiceCanonicalNames()==null || creationContainer.getServiceCanonicalNames().size()!=1){
            throw new BadRequestException("rule argument must contain exactly one service canonical name");
        }
    }

    public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c, List<String> batchString, SecurityContext securityContext) {
        return repository.getByIdOrNull(id, c, batchString, securityContext);
    }

    public FlexiCoreRuleArgument createRuleArgument(RuleArgumentCreate creationContainer, SecurityContext securityContext) {
        FlexiCoreRuleArgument flexiCoreRuleArgument = createRuleArgumentNoMerge(creationContainer, securityContext);
        repository.merge(flexiCoreRuleArgument);
        return flexiCoreRuleArgument;

    }

    public FlexiCoreRuleArgument updateRuleArgument(RuleArgumentUpdate creationContainer, SecurityContext securityContext) {
        FlexiCoreRuleArgument flexiCoreRuleArgument = creationContainer.getFlexiCoreRuleArgument();
        if (updateRuleArgumentNoMerge(flexiCoreRuleArgument, creationContainer)) {
            repository.merge(flexiCoreRuleArgument);

        }
        return flexiCoreRuleArgument;

    }

    private FlexiCoreRuleArgument createRuleArgumentNoMerge(RuleArgumentCreate creationContainer, SecurityContext securityContext) {
        FlexiCoreRuleArgument flexiCoreRuleArgument = FlexiCoreRuleArgument.s().CreateUnchecked(creationContainer.getName(), securityContext);
        flexiCoreRuleArgument.Init();
        updateRuleArgumentNoMerge(flexiCoreRuleArgument, creationContainer);
        return flexiCoreRuleArgument;
    }

    private boolean updateRuleArgumentNoMerge(FlexiCoreRuleArgument flexiCoreRuleArgument, RuleArgumentCreate creationContainer) {
        boolean update = dynamicInvokersService.updateDynamicExecutionNoMerge(creationContainer, flexiCoreRuleArgument);
        return update;

    }

    public PaginationResponse<FlexiCoreRuleArgument> getAllRuleArguments(RuleArgumentFilter filter, SecurityContext securityContext) {
        List<FlexiCoreRuleArgument> list = repository.listAllRuleArguments(filter, securityContext);
        long count = repository.countAllRuleArguments(filter, securityContext);
        return new PaginationResponse<>(list, filter, count);
    }


    public <T> T findByIdOrNull(Class<T> type, String id) {
        return repository.findByIdOrNull(type, id);
    }
}
