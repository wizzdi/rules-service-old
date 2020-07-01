package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.DataSourceFilter;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
@Extension
@Component
public class DataSourceRepository extends AbstractRepositoryPlugin {

	public List<DataSource> listAllDataSources(
			DataSourceFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<DataSource> q = cb.createQuery(DataSource.class);
		Root<DataSource> r = q.from(DataSource.class);
		List<Predicate> preds = new ArrayList<>();
		addDataSourcePredicate(preds, r, q, cb, filter, securityContext);
		QueryInformationHolder<DataSource> queryInformationHolder = new QueryInformationHolder<>(
				filter, DataSource.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addDataSourcePredicate(List<Predicate> preds,
			Root<DataSource> r, CriteriaQuery<?> q, CriteriaBuilder cb,
			DataSourceFilter filter, SecurityContext securityContext) {
		ScenarioFilter scenarioFilter = filter.getScenarioFilter();
		if (scenarioFilter != null) {
			Subquery<String> sub = getConnectedDataSourcesSubQuery(q, cb,
					securityContext, scenarioFilter);
			preds.add(filter.isConnected()
					? r.get(DataSource_.id).in(sub)
					: cb.not(r.get(DataSource_.id).in(sub)));
		}

	}

	private Subquery<String> getConnectedDataSourcesSubQuery(
			CriteriaQuery<?> q, CriteriaBuilder cb,
			SecurityContext securityContext, ScenarioFilter scenarioFilter) {
		Subquery<String> sub = q.subquery(String.class);
		Root<Scenario> sr = sub.from(Scenario.class);
		List<Predicate> subPreds = new ArrayList<>();
		ScenarioRepository.addScenarioPredicate(subPreds, sr, cb,
				scenarioFilter);
		QueryInformationHolder<Scenario> queryInformationHolder = new QueryInformationHolder<>(
				scenarioFilter, Scenario.class, securityContext);
		prepareQuery(queryInformationHolder, subPreds, cb, sub, sr);
		Join<Scenario, ScenarioToDataSource> join = sr
				.join(Scenario_.scenarioToDataSource);
		Join<ScenarioToDataSource, DataSource> join2 = join
				.join(ScenarioToDataSource_.dataSource);
		subPreds.add(cb.isFalse(join.get(ScenarioToDataSource_.softDelete)));

		Predicate[] subPredsArr = new Predicate[subPreds.size()];
		subPreds.toArray(subPredsArr);
		sub.select(join2.get(ScenarioTrigger_.id)).where(subPredsArr);
		return sub;
	}

	public long countAllDataSources(DataSourceFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<DataSource> r = q.from(DataSource.class);
		List<Predicate> preds = new ArrayList<>();
		addDataSourcePredicate(preds, r, q, cb, filter, securityContext);
		QueryInformationHolder<DataSource> queryInformationHolder = new QueryInformationHolder<>(
				filter, DataSource.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
