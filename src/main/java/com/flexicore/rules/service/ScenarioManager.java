package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.product.interfaces.IEventService;
import com.flexicore.request.ExecuteInvokerRequest;
import com.flexicore.response.ExecuteInvokersResponse;
import com.flexicore.rules.events.ScenarioEvent;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.*;
import com.flexicore.rules.response.EvaluateTriggerResponse;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.apache.commons.io.FileUtils;
import org.pf4j.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.script.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
    private Logger logger;

    @Autowired
    private ApplicationEventPublisher scenarioEventEvent;

    @EventListener
    public void handleTrigger(ScenarioEvent scenarioEvent) {
        logger.info("Scenario Trigger Event " + scenarioEvent
                + "captured by Scenario Manager");
        List<ScenarioTrigger> triggers = scenarioTriggerService.listAllScenarioTriggers(new ScenarioTriggerFilter().setEventCanonicalNames(Collections.singleton(scenarioEvent.getClass().getCanonicalName())), null);
        for (ScenarioTrigger trigger : triggers) {
            EvaluateTriggerResponse evaluateTriggerResponse=evaluateTrigger(new EvaluateTriggerRequest().setScenarioTrigger(trigger).setScenarioEvent(scenarioEvent));
            if(evaluateTriggerResponse.isResult()){
                handleTriggerActive(trigger,scenarioEvent);
            }
        }
        ScenarioToTriggerFilter scenarioToTriggerFilter = new ScenarioToTriggerFilter()
                .setEnabled(true)
                .setFiring(true)
                .setNonDeletedScenarios(true)
                .setScenarioTriggers(triggers);
        List<ScenarioToTrigger> scenarioToTriggers = scenarioToTriggerService.listAllScenarioToTrigger(scenarioToTriggerFilter, null);
        Map<String,Scenario> scenarioMap=scenarioToTriggers.stream().collect(Collectors.toMap(f->f.getScenario().getId(),f->f.getScenario(),(a,b)->a))
        Map<String,List<ScenarioToTrigger>> fireingScenarios= scenarioToTriggers.stream().collect(Collectors.groupingBy(f->f.getScenario().getId()));
        ScenarioToDataSourceFilter scenarioToDataSourceFilter = new ScenarioToDataSourceFilter()
                .setEnabled(true)
                .setScenarios(new ArrayList<>(scenarioMap.values()));
        Map<String,List<ScenarioToDataSource>> scenarioToDataSource=scenarioToDataSourceService.listAllScenarioToDataSource(scenarioToDataSourceFilter,null).stream().collect(Collectors.groupingBy(f->f.getScenario().getId()));
        for (Map.Entry<String, List<ScenarioToTrigger>> entry : fireingScenarios.entrySet()) {
            String scenarioId = entry.getKey();
            Scenario scenario=scenarioMap.get(scenarioId);
            List<ScenarioToTrigger> scenarioToTriggerList=entry.getValue();
            List<ScenarioToDataSource> scenarioToDataSources=scenarioToDataSource.getOrDefault(scenarioId,new ArrayList<>());
            //TODO: evaluate scenario
        }

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
            evaluateTriggerResponse.setResult(res);

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
