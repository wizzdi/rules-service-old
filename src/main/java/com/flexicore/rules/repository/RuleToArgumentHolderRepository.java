package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.Baselink_;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRule_;
import com.flexicore.rules.model.RuleToArgument;
import com.flexicore.rules.model.RuleToArgument_;
import com.flexicore.rules.request.RuleToArgumentHolderFilter;
import com.flexicore.rules.request.RulesFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class RuleToArgumentHolderRepository extends AbstractRepositoryPlugin {

	public List<RuleToArgument> listAllRuleToArguments(
			RuleToArgumentHolderFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RuleToArgument> q = cb.createQuery(RuleToArgument.class);
		Root<RuleToArgument> r = q.from(RuleToArgument.class);
		List<Predicate> preds = new ArrayList<>();
		addRuleToArgumentHolderFilterPredicate(preds, r, cb, filter);
		QueryInformationHolder<RuleToArgument> queryInformationHolder = new QueryInformationHolder<>(
				filter, RuleToArgument.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addRuleToArgumentHolderFilterPredicate(List<Predicate> preds,
			Root<RuleToArgument> r, CriteriaBuilder cb,
			RuleToArgumentHolderFilter filter) {
		if (filter.getRules() != null && !filter.getRules().isEmpty()) {
			Set<String> ids = filter.getRules().parallelStream()
					.map(f -> f.getId()).collect(Collectors.toSet());
			Join<RuleToArgument, FlexiCoreRule> join = r
					.join(RuleToArgument_.flexiCoreRule);
			preds.add(join.get(FlexiCoreRule_.id).in(ids));
		}

	}

	public long countAllRuleToArguments(RuleToArgumentHolderFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<RuleToArgument> r = q.from(RuleToArgument.class);
		List<Predicate> preds = new ArrayList<>();
		addRuleToArgumentHolderFilterPredicate(preds, r, cb, filter);
		QueryInformationHolder<RuleToArgument> queryInformationHolder = new QueryInformationHolder<>(
				filter, RuleToArgument.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
