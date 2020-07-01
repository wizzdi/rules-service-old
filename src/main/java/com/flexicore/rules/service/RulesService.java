package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;
import com.flexicore.model.FileResource;
import com.flexicore.request.ExecuteInvokerRequest;
import com.flexicore.response.ExecuteInvokerResponse;
import com.flexicore.response.ExecuteInvokersResponse;
import com.flexicore.rules.model.*;
import com.flexicore.rules.repository.RulesRepository;
import com.flexicore.rules.request.*;
import com.flexicore.rules.response.EvaluateRuleResponse;
import com.flexicore.security.SecurityContext;
import com.flexicore.service.DynamicInvokersService;
import com.flexicore.service.FileResourceService;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.apache.commons.io.FileUtils;

import javax.script.*;
import javax.ws.rs.BadRequestException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.flexicore.rules.service.LogHolder.flush;
import static com.flexicore.rules.service.LogHolder.getLogger;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class RulesService implements ServicePlugin {
	private static ScriptEngine engine = new ScriptEngineManager()
			.getEngineByName("nashorn");

	@PluginInfo(version = 1)
	@Autowired
	private RulesRepository repository;

	@Autowired
	private DynamicInvokersService dynamicInvokersService;

	@PluginInfo(version = 1)
	@Autowired
	private RuleToArgumentService ruleToArgumentHolderService;

	@Autowired
	private FileResourceService fileResourceService;
	@Autowired
	private Logger logger;

	public void validate(RulesFilter rulesFilter,
			SecurityContext securityContext) {

	}

	public void validate(RuleCreate creationContainer,
			SecurityContext securityContext) {
		String fileResourceId = creationContainer.getEvaluationScriptId();
		FileResource fileResource = fileResourceId != null
				? getByIdOrNull(fileResourceId, FileResource.class, null,
						securityContext) : null;
		if (fileResource == null && fileResourceId != null) {
			throw new BadRequestException("No FileResource with id "
					+ fileResourceId);
		}
		creationContainer.setEvaluationScript(fileResource);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public FlexiCoreRule createRule(RuleCreate creationContainer,
			SecurityContext securityContext) {
		List<Object> toMerge = new ArrayList<>();

		FlexiCoreRule flexiCoreRule = createRuleNoMerge(creationContainer,
				securityContext);
		toMerge.add(flexiCoreRule);

		repository.massMerge(toMerge);
		return flexiCoreRule;

	}

	public FlexiCoreRule updateRule(RuleUpdate creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRule flexiCoreRule = creationContainer.getFlexiCoreRule();
		if (updateRuleNoMerge(flexiCoreRule, creationContainer)) {
			repository.merge(flexiCoreRule);

		}
		return flexiCoreRule;

	}

	private FlexiCoreRule createRuleNoMerge(RuleCreate creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRule flexiCoreRule = FlexiCoreRule.s().CreateUnchecked(
				creationContainer.getName(), securityContext);
		flexiCoreRule.Init();
		updateRuleNoMerge(flexiCoreRule, creationContainer);
		return flexiCoreRule;
	}

	private boolean updateRuleNoMerge(FlexiCoreRule flexiCoreRule,
			RuleCreate creationContainer) {
		boolean update = false;
		if (creationContainer.getName() != null
				&& !creationContainer.getName().equals(flexiCoreRule.getName())) {
			flexiCoreRule.setName(creationContainer.getName());
			update = true;
		}

		if (creationContainer.getDescription() != null
				&& !creationContainer.getDescription().equals(
						flexiCoreRule.getDescription())) {
			flexiCoreRule.setDescription(creationContainer.getDescription());
			update = true;
		}

		if (creationContainer.getEvaluationScript() != null
				&& (flexiCoreRule.getEvaluationScript() == null || !creationContainer
						.getEvaluationScript().equals(
								flexiCoreRule.getEvaluationScript()))) {
			flexiCoreRule.setEvaluationScript(creationContainer
					.getEvaluationScript());
			update = true;
		}

		return update;

	}

	public PaginationResponse<FlexiCoreRule> getAllRules(RulesFilter filter,
			SecurityContext securityContext) {
		List<FlexiCoreRule> list = repository.listAllRules(filter,
				securityContext);
		long count = repository.countAllRules(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public void validate(EvaluateRuleRequest evaluateRuleRequest,
			SecurityContext securityContext) {
		String flexiCoreRuleId = evaluateRuleRequest.getRuleId();
		FlexiCoreRule flexiCoreRule = flexiCoreRuleId != null
				? getByIdOrNull(flexiCoreRuleId, FlexiCoreRule.class, null,
						securityContext) : null;
		if (flexiCoreRule == null) {
			throw new BadRequestException("No FlexiCoreRule with id "
					+ flexiCoreRuleId);
		}
		evaluateRuleRequest.setRule(flexiCoreRule);

		String scenarioId = evaluateRuleRequest.getScenarioId();
		Scenario scenario = scenarioId != null ? getByIdOrNull(scenarioId,
				Scenario.class, null, securityContext) : null;
		if (scenario == null) {
			throw new BadRequestException("No Scenario with id " + scenarioId);
		}
		evaluateRuleRequest.setScenario(scenario);

	}

	public Map<String, ExecuteInvokerRequest> evaluateActionManager(
			ScenarioTriggerEvent scenarioTriggerEvent, Scenario scenario,
			Map<String, ExecuteInvokerRequest> map,
			SecurityContext securityContext) {
		FileResource script = scenario.getActionManagerScript();
		Logger scriptLogger = getLogger(scenario);

		try {
			File file = new File(script.getFullPath());
			ScriptObjectMirror loaded = loadScript(file,
					buildFunctionTableFunction(FunctionTypes.EVALUATE));
			ActionManagerContext actionManagerContext = new ActionManagerContext()
					.setScenarioTriggerEvent(scenarioTriggerEvent)
					.setSecurityContext(securityContext)
					.setLogger(scriptLogger).setActionMap(map)
					.setScenario(scenario);
			Object[] parameters = new Object[1];
			parameters[0] = actionManagerContext;
			String[] res = (String[]) loaded.callMember(
					FunctionTypes.EVALUATE.getFunctionName(), parameters);
			Set<String> idsToRun = Stream.of(res).collect(Collectors.toSet());
			return map
					.entrySet()
					.parallelStream()
					.filter(f -> idsToRun.contains(f.getKey()))
					.collect(
							Collectors.toMap(f -> f.getKey(), f -> f.getValue()));

		} catch (Exception e) {
			logger.log(Level.SEVERE, "failed executing script", e);
			scriptLogger.log(Level.SEVERE,
					"failed executing script: " + e.toString(), e);

		} finally {
			flush(scriptLogger);
		}
		return null;
	}

	public EvaluateRuleResponse evaluateRule(
			EvaluateRuleRequest evaluateRuleRequest,
			SecurityContext securityContext) {
		if (evaluateRuleRequest.getRule() instanceof FlexiCoreRuleOp) {
			return evaluateRuleOp(evaluateRuleRequest, securityContext);
		} else {
			return evaluateRuleNode(evaluateRuleRequest, securityContext);
		}
	}

	private EvaluateRuleResponse evaluateRuleOp(
			EvaluateRuleRequest evaluateRuleRequest,
			SecurityContext securityContext) {
		EvaluateRuleResponse evaluateRuleResponse = new EvaluateRuleResponse();
		FlexiCoreRuleOp flexiCoreRuleOp = (FlexiCoreRuleOp) evaluateRuleRequest
				.getRule();
		List<FlexiCoreRule> flexiCoreRules = listAllRuleLinks(
				new RuleLinkFilter().setFlexiCoreRuleOps(Collections
						.singletonList(flexiCoreRuleOp)), securityContext)
				.parallelStream().map(f -> f.getRuleToEval())
				.collect(Collectors.toList());
		boolean res = false;
		switch (flexiCoreRuleOp.getRuleOpType()) {
			case OR :
				res = flexiCoreRules
						.stream()
						.anyMatch(
								f -> evaluateRule(
										new EvaluateRuleRequest()
												.setScenario(
														evaluateRuleRequest
																.getScenario())
												.setScenarioTriggerEvent(
														evaluateRuleRequest
																.getScenarioTriggerEvent())
												.setRule(f), securityContext)
										.isResult());
				break;
			case AND :
				res = flexiCoreRules
						.stream()
						.allMatch(
								f -> evaluateRule(
										new EvaluateRuleRequest()
												.setScenario(
														evaluateRuleRequest
																.getScenario())
												.setScenarioTriggerEvent(
														evaluateRuleRequest
																.getScenarioTriggerEvent())
												.setRule(f), securityContext)
										.isResult());
				break;
			case NOT :
				res = flexiCoreRules
						.stream()
						.noneMatch(
								f -> evaluateRule(
										new EvaluateRuleRequest()
												.setScenario(
														evaluateRuleRequest
																.getScenario())
												.setScenarioTriggerEvent(
														evaluateRuleRequest
																.getScenarioTriggerEvent())
												.setRule(f), securityContext)
										.isResult());
				break;

		}
		evaluateRuleResponse.setResult(res);
		return evaluateRuleResponse;

	}

	private List<FlexiCoreRuleLink> listAllRuleLinks(
			RuleLinkFilter ruleLinkFilter, SecurityContext securityContext) {
		return repository.listAllRuleLinks(ruleLinkFilter, securityContext);
	}

	private EvaluateRuleResponse evaluateRuleNode(
			EvaluateRuleRequest evaluateRuleRequest,
			SecurityContext securityContext) {
		EvaluateRuleResponse evaluateRuleResponse = new EvaluateRuleResponse();
		FlexiCoreRule flexiCoreRule = evaluateRuleRequest.getRule();
		ScenarioTriggerEvent<?> scenarioTriggerEvent = evaluateRuleRequest
				.getScenarioTriggerEvent();

		List<RuleToArgument> arguments = ruleToArgumentHolderService
				.listRuleToArgument(new RuleToArgumentHolderFilter()
						.setRules(Collections.singletonList(flexiCoreRule)),
						securityContext);
		List<ExecuteInvokerResponse> results = arguments
				.stream()
				.sorted(Comparator.comparing(f -> f.getOrdinal()))
				.map(f -> f.getFlexiCoreRuleArgument())
				.map(f -> f.getDynamicExecution() != null
						? dynamicInvokersService.executeInvoker(
								dynamicInvokersService
										.getExecuteInvokerRequest(
												f.getDynamicExecution(),
												scenarioTriggerEvent,
												securityContext),
								securityContext) : new ExecuteInvokersResponse(
								new ArrayList<>()))
				.filter(f -> f.getResponses() != null
						&& !f.getResponses().isEmpty())
				.map(f -> f.getResponses().get(0)).collect(Collectors.toList());
		FileResource script = flexiCoreRule.getEvaluationScript();
		Logger scriptLogger = getLogger(evaluateRuleRequest.getScenario());
		try {
			File file = new File(script.getFullPath());
			ScriptObjectMirror loaded = loadScript(file,
					buildFunctionTableFunction(FunctionTypes.EVALUATE));
			RuleScriptContext ruleScriptContext = new RuleScriptContext()
					.setSecurityContext(securityContext)
					.setLogger(scriptLogger)
					.setScenarioTriggerEvent(scenarioTriggerEvent);
			Object[] parameters = new Object[results.size() + 1];
			results.toArray(parameters);
			parameters[results.size()] = ruleScriptContext;
			boolean res = (boolean) loaded.callMember(
					FunctionTypes.EVALUATE.getFunctionName(), parameters);
			evaluateRuleResponse.setResult(res);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "failed executing script", e);
			scriptLogger.log(Level.SEVERE,
					"failed executing script: " + e.toString(), e);
		} finally {
			flush(scriptLogger);
		}

		return evaluateRuleResponse;

	}

	public boolean evaluateTriggerManager(ScenarioToTrigger scenarioToTrigger,
			ScenarioTriggerEvent scenarioTriggerEvent) {
		FileResource script = scenarioToTrigger.getTriggerManager()
				.getTriggerManagerScript();
		Logger scriptLogger = getLogger(scenarioToTrigger.getScenario());

		try {
			File file = new File(script.getFullPath());
			TriggerManagerContext triggerManagerContext = new TriggerManagerContext()
					.setScenarioTriggerEvent(scenarioTriggerEvent)
					.setScenarioTrigger(scenarioToTrigger.getScenarioTrigger())
					.setScenario(scenarioToTrigger.getScenario())
					.setLogger(scriptLogger);
			ScriptObjectMirror loaded = loadScript(file,
					buildFunctionTableFunction(FunctionTypes.EVALUATE));
			Object[] parameters = new Object[1];
			parameters[0] = triggerManagerContext;

			return (boolean) loaded.callMember(
					FunctionTypes.EVALUATE.getFunctionName(), parameters);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "failed executing script", e);
			scriptLogger.log(Level.SEVERE,
					"failed executing script: " + e.toString(), e);
		} finally {
			flush(scriptLogger);
		}

		return false;
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

	public void validate(RuleCreateOp creationContainer,
			SecurityContext securityContext) {
		if (creationContainer.getRuleOpType() == null) {
			throw new BadRequestException("rule op type may not be null");
		}

	}

	public FlexiCoreRuleOp createRuleOp(RuleCreateOp creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRuleOp flexiCoreRuleOp = createRuleOpNoMerge(
				creationContainer, securityContext);
		repository.merge(flexiCoreRuleOp);
		return flexiCoreRuleOp;

	}

	private FlexiCoreRuleOp createRuleOpNoMerge(RuleCreateOp creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRuleOp flexiCoreRuleOp = FlexiCoreRuleOp.s().CreateUnchecked(
				creationContainer.getName(), securityContext);
		flexiCoreRuleOp.Init();
		updateRuleOpNoMerge(flexiCoreRuleOp, creationContainer);
		return flexiCoreRuleOp;
	}

	private boolean updateRuleOpNoMerge(FlexiCoreRuleOp flexiCoreRuleOp,
			RuleCreateOp creationContainer) {
		boolean update = false;
		if (creationContainer.getName() != null
				&& !creationContainer.getName().equals(
						flexiCoreRuleOp.getName())) {
			flexiCoreRuleOp.setName(creationContainer.getName());
			update = true;
		}

		if (creationContainer.getDescription() != null
				&& !creationContainer.getDescription().equals(
						flexiCoreRuleOp.getDescription())) {
			flexiCoreRuleOp.setDescription(creationContainer.getDescription());
			update = true;
		}
		if (creationContainer.getRuleOpType() != null
				&& (flexiCoreRuleOp.getRuleOpType() == null || !creationContainer
						.getRuleOpType()
						.equals(flexiCoreRuleOp.getRuleOpType()))) {
			flexiCoreRuleOp.setRuleOpType(creationContainer.getRuleOpType());
			update = true;
		}
		return update;
	}

	public FlexiCoreRuleOp updateRuleOp(RuleUpdateOp ruleUpdate,
			SecurityContext securityContext) {
		FlexiCoreRuleOp flexiCoreRuleOp = ruleUpdate.getFlexiCoreRuleOp();
		if (updateRuleOpNoMerge(flexiCoreRuleOp, ruleUpdate)) {
			repository.merge(flexiCoreRuleOp);
		}
		return flexiCoreRuleOp;
	}

	public void validate(RuleLinkCreate creationContainer,
			SecurityContext securityContext) {
		String ruleOpId = creationContainer.getRuleOpId();
		FlexiCoreRuleOp flexiCoreRuleOp = ruleOpId != null ? getByIdOrNull(
				ruleOpId, FlexiCoreRuleOp.class, null, securityContext) : null;
		if (flexiCoreRuleOp == null
				&& (!(creationContainer instanceof RuleLinkUpdate) || ruleOpId != null)) {
			throw new BadRequestException("No FlexiCoreRuleOp with id "
					+ ruleOpId);
		}
		creationContainer.setFlexiCoreRuleOp(flexiCoreRuleOp);

		String ruleid = creationContainer.getRuleid();
		FlexiCoreRule flexiCoreRule = ruleid != null ? getByIdOrNull(ruleid,
				FlexiCoreRule.class, null, securityContext) : null;
		if (flexiCoreRule == null
				&& (!(creationContainer instanceof RuleLinkUpdate) || ruleid != null)) {
			throw new BadRequestException("No FlexiCoreRule with id " + ruleid);
		}
		creationContainer.setFlexiCoreRule(flexiCoreRule);

	}

	public FlexiCoreRuleLink createRuleLink(RuleLinkCreate creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRuleLink flexiCoreRuleLink = createRuleLinkNoMerge(
				creationContainer, securityContext);
		repository.merge(flexiCoreRuleLink);
		return flexiCoreRuleLink;
	}

	private FlexiCoreRuleLink createRuleLinkNoMerge(
			RuleLinkCreate creationContainer, SecurityContext securityContext) {
		FlexiCoreRuleLink flexiCoreRuleLink = FlexiCoreRuleLink.s()
				.CreateUnchecked("RuleLink", securityContext);
		flexiCoreRuleLink.Init();
		updateRuleLinkNoMerge(flexiCoreRuleLink, creationContainer);
		return flexiCoreRuleLink;
	}

	private boolean updateRuleLinkNoMerge(FlexiCoreRuleLink flexiCoreRuleLink,
			RuleLinkCreate creationContainer) {
		boolean update = false;
		if (creationContainer.getFlexiCoreRule() != null
				&& (flexiCoreRuleLink.getRuleToEval() == null || !creationContainer
						.getFlexiCoreRule().getId()
						.equals(flexiCoreRuleLink.getRuleToEval().getId()))) {
			flexiCoreRuleLink.setRuleToEval(creationContainer
					.getFlexiCoreRule());
			update = true;
		}

		if (creationContainer.getFlexiCoreRuleOp() != null
				&& (flexiCoreRuleLink.getRuleOp() == null || !creationContainer
						.getFlexiCoreRuleOp().getId()
						.equals(flexiCoreRuleLink.getRuleOp().getId()))) {
			flexiCoreRuleLink.setRuleOp(creationContainer.getFlexiCoreRuleOp());
			update = true;
		}
		return update;
	}

	public FlexiCoreRuleLink updateRuleLink(RuleLinkUpdate ruleLinkUpdate,
			SecurityContext securityContext) {
		FlexiCoreRuleLink flexiCoreRuleLink = ruleLinkUpdate
				.getFlexiCoreRuleLink();
		if (updateRuleLinkNoMerge(flexiCoreRuleLink, ruleLinkUpdate)) {
			repository.merge(flexiCoreRuleLink);
		}
		return flexiCoreRuleLink;
	}

	public void validate(RuleLinkFilter ruleLinkFilter,
			SecurityContext securityContext) {
		Set<String> opIds = ruleLinkFilter.getRuleOpsIds();
		Map<String, FlexiCoreRuleOp> opMap = opIds.isEmpty()
				? new HashMap<>()
				: repository
						.listByIds(FlexiCoreRuleOp.class, opIds,
								securityContext).parallelStream()
						.collect(Collectors.toMap(f -> f.getId(), f -> f));
		opIds.removeAll(opMap.keySet());
		if (!opIds.isEmpty()) {
			throw new BadRequestException("No Rule Ops with ids " + opIds);
		}
		ruleLinkFilter.setFlexiCoreRuleOps(new ArrayList<>(opMap.values()));

		Set<String> rulesIds = ruleLinkFilter.getRulesIds();
		Map<String, FlexiCoreRule> ruleMap = rulesIds.isEmpty()
				? new HashMap<>()
				: repository
						.listByIds(FlexiCoreRule.class, rulesIds,
								securityContext).parallelStream()
						.collect(Collectors.toMap(f -> f.getId(), f -> f));
		rulesIds.removeAll(ruleMap.keySet());
		if (!rulesIds.isEmpty()) {
			throw new BadRequestException("No Rules with ids " + rulesIds);
		}
		ruleLinkFilter.setFlexiCoreRules(new ArrayList<>(ruleMap.values()));
	}

	public PaginationResponse<FlexiCoreRuleLink> getAllRuleLinks(
			RuleLinkFilter ruleLinkFilter, SecurityContext securityContext) {
		List<FlexiCoreRuleLink> list = listAllRuleLinks(ruleLinkFilter,
				securityContext);
		long count = repository.countAllRuleLinks(ruleLinkFilter,
				securityContext);
		return new PaginationResponse<>(list, ruleLinkFilter, count);
	}

}
