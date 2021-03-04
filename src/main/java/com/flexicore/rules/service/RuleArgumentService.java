package com.flexicore.rules.service;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.data.jsoncontainers.PaginationResponse;
import com.flexicore.interfaces.ServicePlugin;
import com.flexicore.model.Baseclass;

import com.flexicore.rules.model.FlexiCoreRuleArgument;
import com.flexicore.rules.repository.RuleArgumentRepository;
import com.flexicore.rules.request.RuleArgumentCreate;
import com.flexicore.rules.request.RuleArgumentFilter;
import com.flexicore.rules.request.RuleArgumentUpdate;
import com.flexicore.security.SecurityContext;

import javax.ws.rs.BadRequestException;
import java.util.List;

import com.wizzdi.flexicore.boot.dynamic.invokers.model.DynamicExecution;
import com.wizzdi.flexicore.boot.dynamic.invokers.service.DynamicExecutionService;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@PluginInfo(version = 1)
@Extension
@Component
public class RuleArgumentService implements ServicePlugin {

	@PluginInfo(version = 1)
	@Autowired
	private RuleArgumentRepository repository;
	@Autowired
	private DynamicExecutionService dynamicExecutionService;

	public void validate(RuleArgumentFilter ruleArgumentArgumentFilter,
			SecurityContext securityContext) {

	}

	public void validate(RuleArgumentCreate creationContainer,
			SecurityContext securityContext) {
		String dynamicExecutionId = creationContainer.getDynamicExecutionId();
		DynamicExecution dynamicExecution = dynamicExecutionId != null
				? dynamicExecutionService.getByIdOrNull(dynamicExecutionId, DynamicExecution.class, securityContext) : null;
		if (dynamicExecution == null && dynamicExecutionId != null) {
			throw new BadRequestException("No Dynamic Execution With id "
					+ dynamicExecutionId);
		}
		if (dynamicExecution != null
				&& dynamicExecution.getServiceCanonicalNames().size() != 1) {
			throw new BadRequestException(
					"dynamic execution for rule argument must contain exactly one service canonical name");
		}
		creationContainer.setDynamicExecution(dynamicExecution);

	}

	public <T extends Baseclass> T getByIdOrNull(String id, Class<T> c,
			List<String> batchString, SecurityContext securityContext) {
		return repository.getByIdOrNull(id, c, batchString, securityContext);
	}

	public FlexiCoreRuleArgument createRuleArgument(
			RuleArgumentCreate creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRuleArgument flexiCoreRuleArgument = createRuleArgumentNoMerge(
				creationContainer, securityContext);
		repository.merge(flexiCoreRuleArgument);
		return flexiCoreRuleArgument;

	}

	public FlexiCoreRuleArgument updateRuleArgument(
			RuleArgumentUpdate creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRuleArgument flexiCoreRuleArgument = creationContainer
				.getFlexiCoreRuleArgument();
		if (updateRuleArgumentNoMerge(flexiCoreRuleArgument, creationContainer)) {
			repository.merge(flexiCoreRuleArgument);

		}
		return flexiCoreRuleArgument;

	}

	private FlexiCoreRuleArgument createRuleArgumentNoMerge(
			RuleArgumentCreate creationContainer,
			SecurityContext securityContext) {
		FlexiCoreRuleArgument flexiCoreRuleArgument = new FlexiCoreRuleArgument(creationContainer.getName(), securityContext);
		updateRuleArgumentNoMerge(flexiCoreRuleArgument, creationContainer);
		return flexiCoreRuleArgument;
	}

	private boolean updateRuleArgumentNoMerge(
			FlexiCoreRuleArgument flexiCoreRuleArgument,
			RuleArgumentCreate ruleArgumentCreate) {
		boolean update = false;
		if (ruleArgumentCreate.getName() != null
				&& !ruleArgumentCreate.getName().equals(
						flexiCoreRuleArgument.getName())) {
			flexiCoreRuleArgument.setName(ruleArgumentCreate.getName());
			update = true;
		}
		if (ruleArgumentCreate.getDescription() != null
				&& !ruleArgumentCreate.getDescription().equals(
						flexiCoreRuleArgument.getDescription())) {
			flexiCoreRuleArgument.setDescription(ruleArgumentCreate
					.getDescription());
			update = true;
		}
		if (ruleArgumentCreate.getDynamicExecution() != null
				&& (flexiCoreRuleArgument.getDynamicExecution() == null || !ruleArgumentCreate
						.getDynamicExecution()
						.getId()
						.equals(flexiCoreRuleArgument.getDynamicExecution()
								.getId()))) {
			flexiCoreRuleArgument.setDynamicExecution(ruleArgumentCreate
					.getDynamicExecution());
			update = true;
		}
		return update;

	}

	public PaginationResponse<FlexiCoreRuleArgument> getAllRuleArguments(
			RuleArgumentFilter filter, SecurityContext securityContext) {
		List<FlexiCoreRuleArgument> list = repository.listAllRuleArguments(
				filter, securityContext);
		long count = repository.countAllRuleArguments(filter, securityContext);
		return new PaginationResponse<>(list, filter, count);
	}

	public <T> T findByIdOrNull(Class<T> type, String id) {
		return repository.findByIdOrNull(type, id);
	}
}
