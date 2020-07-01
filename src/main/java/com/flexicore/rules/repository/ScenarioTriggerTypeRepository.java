package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioTriggerTypeFilter;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioTriggerTypeRepository extends AbstractRepositoryPlugin {

	public List<ScenarioTriggerType> listAllScenarioTriggerTypes(
			ScenarioTriggerTypeFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScenarioTriggerType> q = cb.createQuery(ScenarioTriggerType.class);
		Root<ScenarioTriggerType> r = q.from(ScenarioTriggerType.class);
		List<Predicate> preds = new ArrayList<>();
		addScenarioTriggerTypePredicate(preds, r, q, cb, filter, securityContext);
		QueryInformationHolder<ScenarioTriggerType> queryInformationHolder = new QueryInformationHolder<>(filter, ScenarioTriggerType.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addScenarioTriggerTypePredicate(List<Predicate> preds,
			Root<ScenarioTriggerType> r, CriteriaQuery<?> q, CriteriaBuilder cb,
			ScenarioTriggerTypeFilter filter, SecurityContext securityContext) {


	}


	public long countAllScenarioTriggerTypes(ScenarioTriggerTypeFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<ScenarioTriggerType> r = q.from(ScenarioTriggerType.class);
		List<Predicate> preds = new ArrayList<>();
		addScenarioTriggerTypePredicate(preds, r, q, cb, filter, securityContext);
		QueryInformationHolder<ScenarioTriggerType> queryInformationHolder = new QueryInformationHolder<>(
				filter, ScenarioTriggerType.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
