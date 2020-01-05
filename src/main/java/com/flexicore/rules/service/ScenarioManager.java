package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.request.ExecuteDynamicExecution;
import com.flexicore.response.ExecuteInvokersResponse;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.EvaluateRuleRequest;
import com.flexicore.rules.request.ScenarioToActionFilter;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerEvent;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.event.ObservesAsync;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@ApplicationScoped
public class ScenarioManager implements ServicePlugin {

    @Inject
    @PluginInfo(version = 1)
    private IEventService eventService;


    @Inject
    @PluginInfo(version = 1)
    private ScenarioToTriggerService scenarioToTriggerService;

    @Inject
    @PluginInfo(version = 1)
    private ScenarioToActionService scenarioToActionService;

    @Inject
    @PluginInfo(version = 1)
    private RulesService rulesService;

    @Inject
    private DynamicInvokersService dynamicInvokersService;

    @Inject
    private Logger logger;

    @Inject
    private Event<ScenarioEvent> scenarioEventEvent;


    public void handleTrigger(@ObservesAsync ScenarioTriggerEvent scenarioTriggerEvent) {
        logger.info("Scenario Trigger Event " + scenarioTriggerEvent + "captured by Scenario Manager");
        SecurityContext securityContext = scenarioTriggerEvent.getSecurityContext();
        ScenarioTrigger scenarioTrigger = scenarioTriggerEvent.getScenarioTrigger();
        List<Scenario> activeScenarios = scenarioToTriggerService.listAllScenarioToTrigger(new ScenarioToTriggerFilter().setEnabled(true).setScenarioTriggers(Collections.singletonList(scenarioTrigger)), securityContext).stream().map(ScenarioToTrigger::getScenario).filter(distinctByKey(Baseclass::getId)).filter(f -> f.getFlexiCoreRule() != null && rulesService.evaluateRule(new EvaluateRuleRequest().setScenarioTriggerEvent(scenarioTriggerEvent).setRule(f.getFlexiCoreRule()), securityContext).isResult()).collect(Collectors.toList());
        if (!activeScenarios.isEmpty()) {
            logger.info("Trigger had caused activated scenarios: "+activeScenarios.parallelStream().map(Baseclass::getId).collect(Collectors.joining(",")));
            List<ScenarioAction> scenarioActions = scenarioToActionService.listAllScenarioToAction(new ScenarioToActionFilter().setEnabled(true).setScenarios(activeScenarios), securityContext).parallelStream().map(ScenarioToAction::getScenarioAction).filter(f -> f.getDynamicExecution() != null).collect(Collectors.toList());
            Set<String> executedActions=new HashSet<>();
            for (ScenarioAction scenarioAction : scenarioActions) {
                ExecuteInvokersResponse response=dynamicInvokersService.executeInvoker(dynamicInvokersService.getExecuteInvokerRequest(scenarioAction.getDynamicExecution(),scenarioTriggerEvent, securityContext),securityContext);
                logger.info("invocation of scenario action "+scenarioAction.getId()+"resulted in "+response);
                executedActions.add(scenarioAction.getId());
            }
            ScenarioEvent scenarioEvent = scenarioTriggerEvent.getScenarioEvent();
            if(scenarioEvent !=null){
                scenarioEvent.setExecutedActions(executedActions);
                scenarioEvent.addToHumanReadableString("Executed Actions were: "+executedActions);
                scenarioEvent.setScenarioHints(activeScenarios.parallelStream().map(Scenario::getScenarioHint).filter(Objects::nonNull).collect(Collectors.toSet()));
                eventService.merge(scenarioEvent);
                scenarioEventEvent.fireAsync(scenarioEvent);
            }
        }
        else{
            logger.info("Trigger had caused no activated scenarios");
        }
    }


    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
