package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.request.ExecuteDynamicExecution;
import com.flexicore.request.ExecuteInvokerRequest;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.events.ScenarioSavableEvent;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.*;
import com.flexicore.rules.response.EvaluateScenarioResponse;
import com.flexicore.rules.response.EvaluateTriggerResponse;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flexicore.rules.service.LogHolder.flush;
import static com.flexicore.rules.service.LogHolder.getLogger;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioManager implements ServicePlugin {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioManager.class);

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");


    @PluginInfo(version = 1)
    @Autowired
    private ScenarioToTriggerService scenarioToTriggerService;

    @PluginInfo(version = 1)
    @Autowired
    private ScenarioToDataSourceService scenarioToDataSourceService;

    @PluginInfo(version = 1)
    @Autowired
    private ScenarioToActionService scenarioToActionService;

    @Autowired
    @PluginInfo(version = 1)
    private ScenarioTriggerService scenarioTriggerService;


    @Autowired
    private DynamicInvokersService dynamicInvokersService;



    @Autowired
    @PluginInfo(version = 1)
    private ScenarioSavableEventService scenarioEventService;


    private boolean isActive(ScenarioTrigger trigger) {
        return trigger.getActiveTill() != null && trigger.getActiveTill().isAfter(OffsetDateTime.now());
    }

    @Async
    @EventListener
    public void handleTrigger(ScenarioEvent scenarioEvent) {
        logger.info("Scenario Trigger Event " + scenarioEvent + "captured by Scenario Manager");
        SecurityContext securityContext=scenarioEvent.getSecurityContext();
        List<ScenarioTrigger> triggers = scenarioTriggerService.listAllScenarioTriggers(new ScenarioTriggerFilter().setEventCanonicalNames(Collections.singleton(scenarioEvent.getClass().getCanonicalName())), securityContext);
        List<ScenarioTrigger> activeTriggers = new ArrayList<>();
        List<Object> toMerge = new ArrayList<>();
        for (ScenarioTrigger trigger : triggers) {
            if (!isValid(trigger)) {
                logger.debug("Trigger " + trigger.getName() + "(" + trigger.getId() + ") invalid");
                continue;
            }
            EvaluateTriggerResponse evaluateTriggerResponse = evaluateTrigger(new EvaluateTriggerRequest().setScenarioTrigger(trigger).setScenarioEvent(scenarioEvent));
            if (evaluateTriggerResponse.isActive()) {
                activeTriggers.add(trigger);
            }
            if (changeTriggerState(trigger, scenarioEvent, evaluateTriggerResponse)) {
                logger.info("Trigger " + trigger.getName() + "(" + trigger.getId() + ") state changed to Active till" + trigger.getActiveTill());
                toMerge.add(trigger);
            } else {
                logger.debug("Trigger " + trigger.getName() + "(" + trigger.getId() + ") state stayed Active till" + trigger.getActiveTill());
            }


        }
        scenarioTriggerService.massMerge(toMerge);

        if (activeTriggers.isEmpty()) {
            logger.debug("No Active triggers for event " + scenarioEvent);
            return;
        }

        ScenarioToTriggerFilter scenarioToTriggerFilter = new ScenarioToTriggerFilter()
                .setEnabled(true)
                .setFiring(true)
                .setNonDeletedScenarios(true)
                .setScenarioTriggers(activeTriggers);
        List<ScenarioToTrigger> scenarioToTriggers = activeTriggers.isEmpty() ? new ArrayList<>() : scenarioToTriggerService.listAllScenarioToTrigger(scenarioToTriggerFilter, securityContext);
        Map<String, Scenario> scenarioMap = scenarioToTriggers.stream().collect(Collectors.toMap(f -> f.getScenario().getId(), f -> f.getScenario(), (a, b) -> a));
        Map<String, List<ScenarioToTrigger>> fireingScenarios = scenarioToTriggers.stream().collect(Collectors.groupingBy(f -> f.getScenario().getId()));
        ScenarioToDataSourceFilter scenarioToDataSourceFilter = new ScenarioToDataSourceFilter()
                .setEnabled(true)
                .setScenarios(new ArrayList<>(scenarioMap.values()));
        Map<String, List<ScenarioToDataSource>> scenarioToDataSource = scenarioMap.isEmpty() ? new HashMap<>() : scenarioToDataSourceService.listAllScenarioToDataSource(scenarioToDataSourceFilter, securityContext).stream().collect(Collectors.groupingBy(f -> f.getScenario().getId()));
        List<EvaluateScenarioResponse> evaluateScenarioResponses = new ArrayList<>();
        List<ScenarioToAction> scenarioToActions =scenarioMap.isEmpty()?new ArrayList<>(): scenarioToActionService.listAllScenarioToAction(new ScenarioToActionFilter().setEnabled(true).setScenarios(new ArrayList<>(scenarioMap.values())), securityContext);
        Map<String, List<ScenarioToAction>> actions = scenarioToActions.stream().collect(Collectors.groupingBy(f -> f.getScenario().getId()));

        for (Map.Entry<String, List<ScenarioToTrigger>> entry : fireingScenarios.entrySet()) {
            String scenarioId = entry.getKey();
            Scenario scenario = scenarioMap.get(scenarioId);
            List<ScenarioTrigger> scenarioToTriggerList = entry.getValue().stream().sorted(Comparator.comparing(f -> f.getOrdinal())).map(f -> f.getScenarioTrigger()).collect(Collectors.toList());
            List<DataSource> scenarioToDataSources = scenarioToDataSource.getOrDefault(scenarioId, new ArrayList<>()).stream().sorted(Comparator.comparing(f -> f.getOrdinal())).map(f -> f.getDataSource()).collect(Collectors.toList());
            Map<String,ExecuteInvokerRequest> scenarioActions=actions.getOrDefault(scenarioId,new ArrayList<>()).stream().map(f->f.getScenarioAction()).collect(Collectors.toMap(f->f.getId(),f->dynamicInvokersService.getExecuteInvokerRequest(f.getDynamicExecution(),securityContext),(a,b)->a));

            EvaluateScenarioRequest evaluateScenarioRequest = new EvaluateScenarioRequest()
                    .setScenario(scenario)
                    .setScenarioEvent(scenarioEvent)
                    .setScenarioTriggers(scenarioToTriggerList)
                    .setDataSources(scenarioToDataSources)
                    .setActions(scenarioActions);
            EvaluateScenarioResponse evaluateScenarioResponse = evaluateScenario(evaluateScenarioRequest);
            evaluateScenarioResponses.add(evaluateScenarioResponse);

        }
        for (EvaluateScenarioResponse evaluateScenarioResponse : evaluateScenarioResponses) {
            if(evaluateScenarioResponse.getActions()!=null){
                for (ExecuteInvokerRequest executeInvokerRequest : evaluateScenarioResponse.getActions().values()) {
                    dynamicInvokersService.executeInvoker(executeInvokerRequest,securityContext);
                }
            }
        }


    }

    private boolean isValid(ScenarioTrigger trigger) {
        OffsetDateTime now = OffsetDateTime.now();
        return triggerValidTimes(now, trigger) && scenarioCoolDown(now, trigger);
    }

    private boolean scenarioCoolDown(OffsetDateTime now, ScenarioTrigger trigger) {
        OffsetDateTime cooldownMin = now.plus(trigger.getCooldownIntervalMs(), ChronoUnit.MILLIS);
        boolean cooldown = trigger.getLastActivated() == null || cooldownMin.isAfter(trigger.getLastActivated());
        if (!cooldown) {
            logger.debug("Trigger " + trigger.getName() + "(" + trigger.getId() + ") invalid cooldown (" + cooldownMin + " vs " + now + ")");

        }
        return cooldown;
    }

    private boolean triggerValidTimes(OffsetDateTime now, ScenarioTrigger trigger) {
        OffsetDateTime start = trigger.getValidFrom().withDayOfYear(now.getDayOfYear()).withYear(now.getYear());
        OffsetDateTime end = trigger.getActiveTill().withDayOfYear(now.getDayOfYear()).withYear(now.getYear());
        boolean validTimes = (now.isAfter(start) || now.equals(start)) && (now.isBefore(end) || now.equals(end));
        if (!validTimes) {
            logger.debug("Trigger " + trigger.getName() + "(" + trigger.getId() + ") invalid times (" + start + "-" + end + " vs " + now + ")");
        }
        return validTimes;
    }


    private EvaluateScenarioResponse evaluateScenario(EvaluateScenarioRequest evaluateScenarioRequest) {
        EvaluateScenarioResponse evaluateScenarioResponse = new EvaluateScenarioResponse()
                .setEvaluateScenarioRequest(evaluateScenarioRequest);
        List<ScenarioTrigger> scenarioTriggers = evaluateScenarioRequest.getScenarioTriggers();
        List<DataSource> scenarioToDataSources = evaluateScenarioRequest.getDataSources();
        ScenarioEvent scenarioEvent = evaluateScenarioRequest.getScenarioEvent();
        Scenario scenario = evaluateScenarioRequest.getScenario();
        FileResource script = scenario.getEvaluatingJSCode();
        java.util.logging.Logger scenarioTriggerLogger = getLogger(scenario.getId(), scenario.getLogFileResource().getFullPath());
        try {
            File file = new File(script.getFullPath());
            ScriptObjectMirror loaded = loadScript(file,
                    buildFunctionTableFunction(FunctionTypes.EVALUATE));
            Map<String, ExecuteInvokerRequest> actions = evaluateScenarioRequest.getActions();
            EvaluateScenarioScriptContext scenarioEventScriptContext = new EvaluateScenarioScriptContext(this::fetchEvent)
                    .setScenario(evaluateScenarioRequest.getScenario())
                    .setActions(actions)
                    .setLogger(scenarioTriggerLogger)
                    .setScenarioEvent(scenarioEvent)
                    .setScenarioToDataSources(scenarioToDataSources)
                    .setScenarioTriggers(scenarioTriggers);
            Object[] parameters = new Object[]{scenarioEventScriptContext};
            String[] res = (String[]) loaded.callMember(
                    FunctionTypes.EVALUATE.getFunctionName(), parameters);
            Set<String> idsToRun = Stream.of(res).collect(Collectors.toSet());
            Map<String, ExecuteInvokerRequest> filtered = actions.entrySet().parallelStream().filter(f -> idsToRun.contains(f.getKey())).collect(Collectors.toMap(f -> f.getKey(), f -> f.getValue()));
            evaluateScenarioResponse.setActions(filtered);

        } catch (Exception e) {
            logger.error( "failed executing script", e);
            scenarioTriggerLogger.log(Level.SEVERE,
                    "failed executing script: " + e.toString(), e);
        } finally {
            flush(scenarioTriggerLogger);
        }

        return evaluateScenarioResponse;
    }

    private ScenarioSavableEvent fetchEvent(String eventId) {
        if (eventId == null) {
            return null;
        }
        List<ScenarioSavableEvent> scenarioEvents = scenarioEventService.listAllScenarioSavableEvents(new ScenarioSavableEventFilter().setScenarioEventIds(Collections.singleton(new ScenarioEventIdFiltering(eventId))));
        return scenarioEvents.isEmpty() ? null : scenarioEvents.get(0);
    }

    private boolean changeTriggerState(ScenarioTrigger trigger, ScenarioEvent scenarioEvent, EvaluateTriggerResponse evaluateTriggerResponse) {
        boolean update = false;
        boolean active = isActive(trigger);
        if (active != evaluateTriggerResponse.isActive()) {
            OffsetDateTime activeTill;
            if (active) {
                activeTill = trigger.getActiveMs() > 0 ? OffsetDateTime.now().plus(trigger.getActiveMs(), ChronoUnit.MILLIS) : OffsetDateTime.MAX;
                trigger.setLastActivated(OffsetDateTime.now());
            } else {
                activeTill = OffsetDateTime.now();
            }
            trigger.setActiveTill(activeTill);


            update = true;
        }
        if (trigger.getLastEventId() == null || (!trigger.getLastEventId().equals(scenarioEvent.getId()))) {
            trigger.setLastEventId(scenarioEvent.getId());
        }
        return update;

    }

    private EvaluateTriggerResponse evaluateTrigger(EvaluateTriggerRequest evaluateTriggerRequest) {
        EvaluateTriggerResponse evaluateTriggerResponse = new EvaluateTriggerResponse();
        ScenarioTrigger scenarioTrigger = evaluateTriggerRequest.getScenarioTrigger();
        ScenarioEvent scenarioEvent = evaluateTriggerRequest.getScenarioEvent();
        FileResource script = scenarioTrigger.getEvaluatingJSCode();
        java.util.logging.Logger scenarioTriggerLogger = getLogger(scenarioTrigger.getId(), scenarioTrigger.getLogFileResource().getFullPath());
        try {
            File file = new File(script.getFullPath());
            ScriptObjectMirror loaded = loadScript(file,
                    buildFunctionTableFunction(FunctionTypes.EVALUATE));
            EvaluateTriggerScriptContext scenarioEventScriptContext = new EvaluateTriggerScriptContext()
                    .setLogger(scenarioTriggerLogger)
                    .setScenarioEvent(scenarioEvent);
            Object[] parameters = new Object[]{scenarioEventScriptContext};
            boolean res = (boolean) loaded.callMember(
                    FunctionTypes.EVALUATE.getFunctionName(), parameters);
            evaluateTriggerResponse.setActive(res);

        } catch (Exception e) {
            logger.error("failed executing script", e);
            scenarioTriggerLogger.log(Level.SEVERE,
                    "failed executing script: " + e.toString(), e);
        } finally {
            flush(scenarioTriggerLogger);
        }

        return evaluateTriggerResponse;

    }

    private ScriptObjectMirror loadScript(File file, String functionTable)
            throws IOException, ScriptException {
        String script = FileUtils
                .readFileToString(file, StandardCharsets.UTF_8);
        script += functionTable;
        CompiledScript compiled = ((Compilable) engine).compile(script);
        ScriptObjectMirror table = (ScriptObjectMirror) compiled.eval();
        return (ScriptObjectMirror) table.call(null);
    }

    private String buildFunctionTableFunction(FunctionTypes... functionTypes) {
        String base = "function () {" + "  return { ";
        String functions = Stream
                .of(functionTypes)
                .map(f -> "'" + f.getFunctionName() + "':"
                        + f.getFunctionName()).collect(Collectors.joining(","));
        base += functions;
        base += "};";
        base += "};";
        return base;

    }
}
