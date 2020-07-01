package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.TriggerManager;
import com.flexicore.rules.request.TriggerManagerFilter;
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
public class TriggerManagerRepository extends AbstractRepositoryPlugin {

	public List<TriggerManager> listAllTriggerManagers(
			TriggerManagerFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TriggerManager> q = cb.createQuery(TriggerManager.class);
		Root<TriggerManager> r = q.from(TriggerManager.class);
		List<Predicate> preds = new ArrayList<>();
		addTriggerManagerPredicate(preds, r, cb, filter);
		QueryInformationHolder<TriggerManager> queryInformationHolder = new QueryInformationHolder<>(
				filter, TriggerManager.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	public static void addTriggerManagerPredicate(List<Predicate> preds,
			Root<TriggerManager> r, CriteriaBuilder cb,
			TriggerManagerFilter filter) {

	}

	public long countAllTriggerManagers(TriggerManagerFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<TriggerManager> r = q.from(TriggerManager.class);
		List<Predicate> preds = new ArrayList<>();
		addTriggerManagerPredicate(preds, r, cb, filter);
		QueryInformationHolder<TriggerManager> queryInformationHolder = new QueryInformationHolder<>(
				filter, TriggerManager.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
