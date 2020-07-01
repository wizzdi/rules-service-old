package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioToDataSourceFilter;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class ScenarioToDataSourceRepository extends AbstractRepositoryPlugin {

	public List<ScenarioToDataSource> listAllScenarioToDataSources(
			ScenarioToDataSourceFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ScenarioToDataSource> q = cb
				.createQuery(ScenarioToDataSource.class);
		Root<ScenarioToDataSource> r = q.from(ScenarioToDataSource.class);
		List<Predicate> preds = new ArrayList<>();
		addScenarioToDataSourcePredicate(preds, r, cb, filter);
		QueryInformationHolder<ScenarioToDataSource> queryInformationHolder = new QueryInformationHolder<>(
				filter, ScenarioToDataSource.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addScenarioToDataSourcePredicate(List<Predicate> preds,
			Root<ScenarioToDataSource> r, CriteriaBuilder cb,
			ScenarioToDataSourceFilter filter) {

		if (filter.getScenarios() != null && !filter.getScenarios().isEmpty()) {
			Set<String> ids = filter.getScenarios().parallelStream()
					.map(f -> f.getId()).collect(Collectors.toSet());
			Join<ScenarioToDataSource, Scenario> join = r
					.join(ScenarioToDataSource_.scenario);
			preds.add(join.get(Scenario_.id).in(ids));
		}

		if (filter.getDataSources() != null
				&& !filter.getDataSources().isEmpty()) {
			Set<String> ids = filter.getDataSources().parallelStream()
					.map(f -> f.getId()).collect(Collectors.toSet());
			Join<ScenarioToDataSource, DataSource> join = r
					.join(ScenarioToDataSource_.dataSource);
			preds.add(join.get(ScenarioToDataSource_.id).in(ids));
		}
		if (filter.getEnabled() != null) {
			preds.add(cb.equal(r.get(ScenarioToDataSource_.enabled),
					filter.getEnabled()));
		}

	}

	public long countAllScenarioToDataSources(ScenarioToDataSourceFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<ScenarioToDataSource> r = q.from(ScenarioToDataSource.class);
		List<Predicate> preds = new ArrayList<>();
		addScenarioToDataSourcePredicate(preds, r, cb, filter);
		QueryInformationHolder<ScenarioToDataSource> queryInformationHolder = new QueryInformationHolder<>(
				filter, ScenarioToDataSource.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
