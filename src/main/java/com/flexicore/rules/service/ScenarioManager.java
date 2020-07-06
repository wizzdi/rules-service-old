package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.FileResource;
import com.flexicore.model.dynamic.DynamicExecution;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.request.ExecuteDynamicExecution;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.*;
import com.flexicore.rules.response.EvaluateScenarioResponse;
import com.flexicore.rules.response.EvaluateTriggerResponse;
import com.flexicore.service.DynamicInvokersService;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flexicore.rules.service.LogHolder.flush;
import static com.flexicore.rules.service.LogHolder.getLogger;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioManager implements ServicePlugin {

    private static ScriptEngine engine = new ScriptEngineManager()
            .getEngineByName("nashorn");
    @PluginInfo(version = 1)
    @Autowired
    private IEventService eventService;

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
    private ActionReplacementService actionReplacementService;

    @Autowired
    private Logger logger;

    @Autowired
    private ScenarioEventService scenarioEventService;

    @Autowired
    private ApplicationEventPublisher scenarioEventEvent;

    private boolean isActive(ScenarioTrigger trigger) {
        return trigger.getActiveTill() != null && trigger.getActiveTill().isAfter(OffsetDateTime.now());
    }

    @EventListener
    public void handleTrigger(ScenarioEvent scenarioEvent) {
        logger.info("Scenario Trigger Event " + scenarioEvent
                + "captured by Scenario Manager");
        List<ScenarioTrigger> triggers = scenarioTriggerService.listAllScenarioTriggers(new ScenarioTriggerFilter().setEventCanonicalNames(Collections.singleton(scenarioEvent.getClass().getCanonicalName())), null);
        List<ScenarioTrigger> activeTriggers = new ArrayList<>();
        List<Object> toMerge = new ArrayList<>();
        for (ScenarioTrigger trigger : triggers) {
            if(!isValid(trigger)){
                logger.fine("Trigger "+trigger.getName()+"("+trigger.getId()+") invalid");
                continue;
            }
            EvaluateTriggerResponse evaluateTriggerResponse = evaluateTrigger(new EvaluateTriggerRequest().setScenarioTrigger(trigger).setScenarioEvent(scenarioEvent));
            if (evaluateTriggerResponse.isActive()) {
                activeTriggers.add(trigger);
            }
            if (changeTriggerState(trigger, scenarioEvent, evaluateTriggerResponse)) {
                logger.info("Trigger " + trigger.getName() + "(" + trigger.getId() + ") state changed to Active till" + trigger.getActiveTill());
                toMerge.add(trigger);
            }
            else{
                logger.fine("Trigger " + trigger.getName() + "(" + trigger.getId() + ") state stayed Active till" + trigger.getActiveTill());
            }


        }
        scenarioTriggerService.massMerge(toMerge);

        if (activeTriggers.isEmpty()) {
            logger.fine("No Active triggers for event "+scenarioEvent);
            return;
        }
        scenarioEvent.setEvaluatedScenarioTriggerIds(activeTriggers.stream().map(f -> f.getId()).collect(Collectors.toSet()));
        scenarioEventService.mergeScenarioEvent(scenarioEvent);

        ScenarioToTriggerFilter scenarioToTriggerFilter = new ScenarioToTriggerFilter()
                .setEnabled(true)
                .setFiring(true)
                .setNonDeletedScenarios(true)
                .setScenarioTriggers(activeTriggers);
        List<ScenarioToTrigger> scenarioToTriggers = activeTriggers.isEmpty() ? new ArrayList<>() : scenarioToTriggerService.listAllScenarioToTrigger(scenarioToTriggerFilter, null);
        Map<String, Scenario> scenarioMap = scenarioToTriggers.stream().collect(Collectors.toMap(f -> f.getScenario().getId(), f -> f.getScenario(), (a, b) -> a));
        Map<String, List<ScenarioToTrigger>> fireingScenarios = scenarioToTriggers.stream().collect(Collectors.groupingBy(f -> f.getScenario().getId()));
        ScenarioToDataSourceFilter scenarioToDataSourceFilter = new ScenarioToDataSourceFilter()
                .setEnabled(true)
                .setScenarios(new ArrayList<>(scenarioMap.values()));
        Map<String, List<ScenarioToDataSource>> scenarioToDataSource = scenarioMap.isEmpty() ? new HashMap<>() : scenarioToDataSourceService.listAllScenarioToDataSource(scenarioToDataSourceFilter, null).stream().collect(Collectors.groupingBy(f -> f.getScenario().getId()));
        List<EvaluateScenarioResponse> evaluateScenarioResponses = new ArrayList<>();
        for (Map.Entry<String, List<ScenarioToTrigger>> entry : fireingScenarios.entrySet()) {
            String scenarioId = entry.getKey();
            Scenario scenario = scenarioMap.get(scenarioId);
            List<ScenarioTrigger> scenarioToTriggerList = entry.getValue().stream().sorted(Comparator.comparing(f -> f.getOrdinal())).map(f -> f.getScenarioTrigger()).collect(Collectors.toList());
            List<DataSource> scenarioToDataSources = scenarioToDataSource.getOrDefault(scenarioId, new ArrayList<>()).stream().sorted(Comparator.comparing(f -> f.getOrdinal())).map(f -> f.getDataSource()).collect(Collectors.toList());
            EvaluateScenarioRequest evaluateScenarioRequest = new EvaluateScenarioRequest()
                    .setScenario(scenario)
                    .setScenarioEvent(scenarioEvent)
                    .setScenarioTriggers(scenarioToTriggerList)
                    .setDataSources(scenarioToDataSources);
            EvaluateScenarioResponse evaluateScenarioResponse = evaluateScenario(evaluateScenarioRequest);
            if (evaluateScenarioResponse.isResult()) {
                evaluateScenarioResponses.add(evaluateScenarioResponse);
            }
        }
        if (!evaluateScenarioResponses.isEmpty()) {
            Map<String, EvaluateScenarioResponse> responseMap = evaluateScenarioResponses.stream().collect(Collectors.toMap(f -> f.getEvaluateScenarioRequest().getScenario().getId(), f -> f));
            List<Scenario> activeScenarios = evaluateScenarioResponses.stream().map(f -> f.getEvaluateScenarioRequest().getScenario()).collect(Collectors.toList());
            List<ScenarioToAction> scenarioToActions = scenarioToActionService.listAllScenarioToAction(new ScenarioToActionFilter().setEnabled(true).setScenarios(activeScenarios), null);
            Map<String, List<ScenarioToAction>> actions = scenarioToActions.stream().collect(Collectors.groupingBy(f -> f.getScenario().getId()));
            List<ActionReplacement> actionReplacements = scenarioToActions.isEmpty() ? new ArrayList<>() : actionReplacementService.listAllActionReplacements(new ActionReplacementFilter().setScenarioToActions(scenarioToActions), null);
            List<ScenarioTrigger> relevantTriggers = new ArrayList<>(actionReplacements.stream().map(f -> f.getScenarioTrigger()).collect(Collectors.toMap(f -> f.getId(), f -> f, (a, b) -> a)).values());
            Map<String, ScenarioEvent> lastScenarioEvents = relevantTriggers.isEmpty() ? new HashMap<>() : scenarioEventService.listByIds(relevantTriggers.stream().map(f -> f.getLastEventId()).collect(Collectors.toSet())).stream().collect(Collectors.toMap(f -> f.getId(), f -> f));
            Map<String, Map<String, List<ActionReplacement>>> actionReplacementsMap = actionReplacements.stream().collect(Collectors.groupingBy(f -> f.getScenarioToAction().getId(), Collectors.groupingBy(f -> f.getScenarioTrigger().getId())));
            for (Scenario activeScenario : activeScenarios) {
                EvaluateScenarioResponse evaluateScenarioResponse = responseMap.get(activeScenario.getId());
                List<ScenarioToAction> scenarioActions = actions.getOrDefault(activeScenario.getId(), new ArrayList<>());
                for (ScenarioToAction scenarioAction : scenarioActions) {
                    Map<String, List<ActionReplacement>> replacements = actionReplacementsMap.get(scenarioAction.getId());
                    executeAction(scenarioAction, replacements, lastScenarioEvents, evaluateScenarioResponse);
                }

            }

        }


    }

    private boolean isValid(ScenarioTrigger trigger) {
        OffsetDateTime now = OffsetDateTime.now();
        return triggerValidTimes(now,trigger)&&scenarioCoolDown(now,trigger);
    }

    private boolean scenarioCoolDown(OffsetDateTime now, ScenarioTrigger trigger) {
        OffsetDateTime cooldownMin = now.plus(trigger.getCooldownIntervalMs(), ChronoUnit.MILLIS);
        boolean cooldown = trigger.getLastActivated() == null || cooldownMin.isAfter(trigger.getLastActivated());
        if(!cooldown){
            logger.fine("Trigger "+trigger.getName()+"("+trigger.getId()+") invalid cooldown ("+cooldownMin+" vs "+now+")");

        }
        return cooldown;
    }

    private boolean triggerValidTimes(OffsetDateTime now,ScenarioTrigger trigger) {
        OffsetDateTime start=trigger.getValidFrom().withDayOfYear(now.getDayOfYear()).withYear(now.getYear());
        OffsetDateTime end=trigger.getActiveTill().withDayOfYear(now.getDayOfYear()).withYear(now.getYear());
        boolean validTimes = (now.isAfter(start) || now.equals(start)) && (now.isBefore(end) || now.equals(end));
        if(!validTimes){
            logger.fine("Trigger "+trigger.getName()+"("+trigger.getId()+") invalid times ("+start +"-"+end+" vs "+now+")");
        }
        return validTimes;
    }

    private void executeAction(ScenarioToAction scenarioAction, Map<String, List<ActionReplacement>> replacements, Map<String, ScenarioEvent> lastScenarioEvents, EvaluateScenarioResponse evaluateScenarioResponse) {
        for (Map.Entry<String, List<ActionReplacement>> entry : replacements.entrySet()) {
            ScenarioEvent scenarioEvent = lastScenarioEvents.get(entry.getKey());
            List<ActionReplacement> replacementsForEvents = entry.getValue();
            for (ActionReplacement replacementsForEvent : replacementsForEvents) {
                try {
                    replace(scenarioAction.getScenarioAction().getDynamicExecution(), scenarioEvent, replacementsForEvent);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "failed replacing values " + replacementsForEvent.getName() + "(" + replacementsForEvent.getId() + ")", e);
                }
            }
        }
        dynamicInvokersService.executeInvoker(new ExecuteDynamicExecution().setDynamicExecution(scenarioAction.getScenarioAction().getDynamicExecution()), null);

    }

    private void replace(DynamicExecution dynamicExecution, ScenarioEvent scenarioEvent, ActionReplacement replacementsForEvent) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        String[] fields = replacementsForEvent.getEventSourcePath().split("\\.");
        Class<?> current = scenarioEvent.getClass();
        Object currentVal = scenarioEvent;
        for (String fieldName : fields) {
            Method method = getGetter(current, fieldName);
            currentVal = method.invoke(currentVal);
            current = currentVal.getClass();
        }
        String[] target = replacementsForEvent.getExecutionTargetPath().split("\\.");
        Class<?> currentTarget = dynamicExecution.getClass();
        Object currentTargetVal = dynamicExecution;
        for (int i = 0; i < target.length - 1; i++) {
            String fieldName = target[i];
            Method method = getGetter(current, fieldName);
            currentTargetVal = method.invoke(currentTargetVal);
        }
        Method setter = getSetter(currentTarget, target[target.length - 1]);
        setter.invoke(currentTargetVal, currentVal);

    }

    private Method getSetter(Class<?> current, String fieldName) throws NoSuchMethodException {
        return current.getMethod("set" + StringUtils.capitalize(fieldName));
    }

    private Method getGetter(Class<?> current, String fieldName) throws NoSuchMethodException {
        try {
            return current.getMethod("get" + StringUtils.capitalize(fieldName));
        } catch (NoSuchMethodException e) {
            return current.getMethod("is" + StringUtils.capitalize(fieldName));

        }
    }

    private EvaluateScenarioResponse evaluateScenario(EvaluateScenarioRequest evaluateScenarioRequest) {
        EvaluateScenarioResponse evaluateScenarioResponse = new EvaluateScenarioResponse()
                .setEvaluateScenarioRequest(evaluateScenarioRequest);
        List<ScenarioTrigger> scenarioTriggers = evaluateScenarioRequest.getScenarioTriggers();
        List<DataSource> scenarioToDataSources = evaluateScenarioRequest.getDataSources();
        ScenarioEvent scenarioEvent = evaluateScenarioRequest.getScenarioEvent();
        Scenario scenario = evaluateScenarioRequest.getScenario();
        FileResource script = scenario.getEvaluatingJSCode();
        Logger scenarioTriggerLogger = getLogger(scenario.getId(), scenario.getLogFileResource().getFullPath());
        try {
            File file = new File(script.getFullPath());
            ScriptObjectMirror loaded = loadScript(file,
                    buildFunctionTableFunction(FunctionTypes.EVALUATE));
            ScenarioEventScriptContext scenarioEventScriptContext = new ScenarioEventScriptContext()
                    .setLogger(scenarioTriggerLogger)
                    .setScenarioEvent(scenarioEvent)
                    .setScenarioToDataSources(scenarioToDataSources)
                    .setScenarioTriggers(scenarioTriggers);
            Object[] parameters = new Object[]{scenarioEventScriptContext};
            boolean res = (boolean) loaded.callMember(
                    FunctionTypes.EVALUATE.getFunctionName(), parameters);
            evaluateScenarioResponse.setResult(res);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed executing script", e);
            scenarioTriggerLogger.log(Level.SEVERE,
                    "failed executing script: " + e.toString(), e);
        } finally {
            flush(scenarioTriggerLogger);
        }

        return evaluateScenarioResponse;
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
        Logger scenarioTriggerLogger = getLogger(scenarioTrigger.getId(), scenarioTrigger.getLogFileResource().getFullPath());
        try {
            File file = new File(script.getFullPath());
            ScriptObjectMirror loaded = loadScript(file,
                    buildFunctionTableFunction(FunctionTypes.EVALUATE));
            ScenarioEventScriptContext scenarioEventScriptContext = new ScenarioEventScriptContext()
                    .setLogger(scenarioTriggerLogger)
                    .setScenarioEvent(scenarioEvent);
            Object[] parameters = new Object[]{scenarioEventScriptContext};
            boolean res = (boolean) loaded.callMember(
                    FunctionTypes.EVALUATE.getFunctionName(), parameters);
            evaluateTriggerResponse.setActive(res);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "failed executing script", e);
            scenarioTriggerLogger.log(Level.SEVERE,
                    "failed executing script: " + e.toString(), e);
        } finally {
            flush(scenarioTriggerLogger);
        }

        return evaluateTriggerResponse;

    }


    public static <T> Predicate<T> distinctByKey(
            Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
