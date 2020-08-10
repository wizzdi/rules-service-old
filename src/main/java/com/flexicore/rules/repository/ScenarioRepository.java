package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.Scenario_;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioRepository extends AbstractRepositoryPlugin {

	public List<Scenario> listAllScenarios(ScenarioFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Scenario> q = cb.createQuery(Scenario.class);
		Root<Scenario> r = q.from(Scenario.class);
		List<Predicate> preds = new ArrayList<>();
		addScenarioPredicate(preds, r, cb, filter);
		QueryInformationHolder<Scenario> queryInformationHolder = new QueryInformationHolder<>(
				filter, Scenario.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public static void addScenarioPredicate(List<Predicate> preds,
			Root<Scenario> r, CriteriaBuilder cb, ScenarioFilter filter) {
		if(filter.getNoLogs()!=null){
			preds.add(filter.getNoLogs()?r.get(Scenario_.logFileResource).isNull():r.get(Scenario_.logFileResource).isNotNull());
		}

	}

	public long countAllScenarios(ScenarioFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<Scenario> r = q.from(Scenario.class);
		List<Predicate> preds = new ArrayList<>();
		addScenarioPredicate(preds, r, cb, filter);
		QueryInformationHolder<Scenario> queryInformationHolder = new QueryInformationHolder<>(
				filter, Scenario.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
