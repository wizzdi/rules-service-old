package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.product.interfaces.IEvent;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.request.ExecuteInvokerRequest;
import com.flexicore.response.ExecuteInvokersResponse;
import com.flexicore.rules.events.IScenarioEvent;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.EvaluateRuleRequest;
import com.flexicore.rules.request.ScenarioToActionFilter;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerEvent;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;

@PluginInfo(version = 1)
@ApplicationScoped
@Extension
@Component
public class ScenarioManager implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private IEventService eventService;

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioToTriggerService scenarioToTriggerService;

	@PluginInfo(version = 1)
	@Autowired
	private ScenarioToActionService scenarioToActionService;

	@PluginInfo(version = 1)
	@Autowired
	private RulesService rulesService;

	@Autowired
	private DynamicInvokersService dynamicInvokersService;

	@Autowired
	private Logger logger;

	@Autowired
	private ApplicationEventPublisher scenarioEventEvent;

	@EventListener
	@Async
	public void handleTrigger(ScenarioTriggerEvent scenarioTriggerEvent) {
		logger.info("Scenario Trigger Event " + scenarioTriggerEvent + "captured by Scenario Manager");
		if (scenarioTriggerEvent.getScenarioTrigger() == null) {
			logger.warning("Scenario Trigger Event " + scenarioTriggerEvent + " had null trigger , ignoring");
			return;
		}
		SecurityContext securityContext = scenarioTriggerEvent.getSecurityContext();
		ScenarioTrigger scenarioTrigger = scenarioTriggerEvent.getScenarioTrigger();
		Map<String, Scenario> activeScenarios = scenarioToTriggerService.listAllScenarioToTrigger(new ScenarioToTriggerFilter().setNonDeletedScenarios(true).setEnabled(true).setScenarioTriggers(Collections.singletonList(scenarioTrigger)), securityContext).stream().filter(f -> f.getTriggerManager() == null || rulesService.evaluateTriggerManager(f, scenarioTriggerEvent)).map(ScenarioToTrigger::getScenario).filter(distinctByKey(Baseclass::getId)).filter(f -> f.getFlexiCoreRule() != null && rulesService.evaluateRule(new EvaluateRuleRequest().setScenario(f).setScenarioTriggerEvent(scenarioTriggerEvent).setRule(f.getFlexiCoreRule()), securityContext).isResult()).collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
		if (!activeScenarios.isEmpty()) {
			logger.info("Trigger had caused activated scenarios: " + activeScenarios.values().parallelStream().map(Baseclass::getId).collect(Collectors.joining(",")));
			Map<String, List<ScenarioAction>> scenarioActions = scenarioToActionService.listAllScenarioToAction(new ScenarioToActionFilter().setEnabled(true).setScenarios(new ArrayList<>(activeScenarios.values())), securityContext).parallelStream().filter(f -> f.getScenarioAction().getDynamicExecution() != null).collect(Collectors.groupingBy(f -> f.getScenario().getId(), Collectors.mapping(f -> f.getScenarioAction(), Collectors.toList())));
			Map<String, ScenarioAction> executedActions = new HashMap<>();
			for (Map.Entry<String, List<ScenarioAction>> entry : scenarioActions
					.entrySet()) {
				Map<String, ScenarioAction> actionsToExecute = entry.getValue().parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a));
				Map<String, ExecuteInvokerRequest> executeInvokerRequests = actionsToExecute.values().parallelStream().collect(Collectors.toMap(f -> f.getId(), f -> dynamicInvokersService.getExecuteInvokerRequest(f.getDynamicExecution(), scenarioTriggerEvent, securityContext)));
				Scenario scenario = activeScenarios.get(entry.getKey());
				Map<String, ExecuteInvokerRequest> filtered = executeInvokerRequests;
				if (scenario.getActionManagerScript() != null) {
					logger.info("scenario " + scenario.getName() + "(" + scenario.getId() + ") has action manager");
					filtered = rulesService.evaluateActionManager(scenarioTriggerEvent, scenario, filtered, securityContext);
				}
				if (filtered == null) {
					logger.warning("Action manager failed");
					return;
				}
				for (Map.Entry<String, ExecuteInvokerRequest> executeInvokerRequestEntry : filtered.entrySet()) {
					ExecuteInvokerRequest executeInvokerRequest = executeInvokerRequestEntry.getValue();
					ExecuteInvokersResponse response = dynamicInvokersService.executeInvoker(executeInvokerRequest, securityContext);
					ScenarioAction scenarioAction = actionsToExecute	.get(executeInvokerRequestEntry.getKey());
					logger.info("invocation of scenario action " + scenarioAction.getId() + "resulted in " + response);
					executedActions.put(scenarioAction.getId(), scenarioAction);
					scenarioAction.getDynamicExecution().setLastExecuted(OffsetDateTime.now());
					dynamicInvokersService.massMerge(Collections.singletonList(scenarioAction.getDynamicExecution()));
				}
			}
			IEvent scenarioEvent = scenarioTriggerEvent.getScenarioEvent();
			if (scenarioEvent instanceof IScenarioEvent) {
				IScenarioEvent iScenarioEvent= (IScenarioEvent) scenarioEvent;
				iScenarioEvent.setExecutedActions(executedActions.keySet());
				iScenarioEvent.addToHumanReadableString("Executed Actions were: " + executedActions.values().stream().map(f -> f.getName()).collect(Collectors.joining(",")));
				iScenarioEvent.setScenarioHints(activeScenarios.values().parallelStream().map(Scenario::getScenarioHint).filter(Objects::nonNull).collect(Collectors.toSet()));

				if( scenarioEvent instanceof com.flexicore.product.model.Event){
					eventService.merge((com.flexicore.product.model.Event) scenarioEvent);

				}
			}
			if(scenarioEvent!=null){
				scenarioEventEvent.publishEvent(scenarioEvent);
			}
		} else {
			logger.info("Trigger had caused no activated scenarios");
		}
	}

	public static <T> Predicate<T> distinctByKey(
			Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}
