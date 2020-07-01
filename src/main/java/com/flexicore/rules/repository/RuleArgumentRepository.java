package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.RuleArgumentFilter;
import com.flexicore.rules.request.RulesFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class RuleArgumentRepository extends AbstractRepositoryPlugin {

	public List<FlexiCoreRuleArgument> listAllRuleArguments(
			RuleArgumentFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<FlexiCoreRuleArgument> q = cb
				.createQuery(FlexiCoreRuleArgument.class);
		Root<FlexiCoreRuleArgument> r = q.from(FlexiCoreRuleArgument.class);
		List<Predicate> preds = new ArrayList<>();
		addRuleArgumentsPredicate(preds, q, r, cb, filter, securityContext);
		QueryInformationHolder<FlexiCoreRuleArgument> queryInformationHolder = new QueryInformationHolder<>(
				filter, FlexiCoreRuleArgument.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addRuleArgumentsPredicate(List<Predicate> preds,
			CriteriaQuery<?> q, Root<FlexiCoreRuleArgument> r,
			CriteriaBuilder cb, RuleArgumentFilter filter,
			SecurityContext securityContext) {
		RulesFilter rulesFilter = filter.getRulesFilter();
		if (rulesFilter != null) {
			Subquery<String> sub = getConnectedRulesSubQuery(q, cb,
					securityContext, rulesFilter);
			preds.add(filter.isConnected() ? r.get(FlexiCoreRuleArgument_.id)
					.in(sub) : cb.not(r.get(FlexiCoreRuleArgument_.id).in(sub)));
		}

	}

	private Subquery<String> getConnectedRulesSubQuery(CriteriaQuery<?> q,
			CriteriaBuilder cb, SecurityContext securityContext,
			RulesFilter ruleFilter) {
		Subquery<String> sub = q.subquery(String.class);
		Root<FlexiCoreRule> sr = sub.from(FlexiCoreRule.class);
		List<Predicate> subPreds = new ArrayList<>();
		RulesRepository.addRulesPredicate(subPreds, sr, cb, ruleFilter);
		QueryInformationHolder<FlexiCoreRule> queryInformationHolder = new QueryInformationHolder<>(
				ruleFilter, FlexiCoreRule.class, securityContext);
		prepareQuery(queryInformationHolder, subPreds, cb, sub, sr);
		Join<FlexiCoreRule, RuleToArgument> join = sr
				.join(FlexiCoreRule_.ruleToExecutionParameters);
		Join<RuleToArgument, FlexiCoreRuleArgument> join2 = join
				.join(RuleToArgument_.flexiCoreRuleArgument);
		subPreds.add(cb.isFalse(join.get(RuleToArgument_.softDelete)));
		Predicate[] subPredsArr = new Predicate[subPreds.size()];
		subPreds.toArray(subPredsArr);
		sub.select(join2.get(FlexiCoreRuleArgument_.id)).where(subPredsArr);
		return sub;
	}

	public long countAllRuleArguments(RuleArgumentFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<FlexiCoreRuleArgument> r = q.from(FlexiCoreRuleArgument.class);
		List<Predicate> preds = new ArrayList<>();
		addRuleArgumentsPredicate(preds, q, r, cb, filter, securityContext);
		QueryInformationHolder<FlexiCoreRuleArgument> queryInformationHolder = new QueryInformationHolder<>(
				filter, FlexiCoreRuleArgument.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
