package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.GenericTriggerFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

@PluginInfo(version = 1)
@Extension
@Component
public class GenericTriggerRepository extends AbstractRepositoryPlugin {

	public List<GenericTrigger> listAllGenericTriggers(
			GenericTriggerFilter filter, SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<GenericTrigger> q = cb.createQuery(GenericTrigger.class);
		Root<GenericTrigger> r = q.from(GenericTrigger.class);
		List<Predicate> preds = new ArrayList<>();
		addGenericTriggerPredicate(preds, r, q, cb, filter, securityContext);
		QueryInformationHolder<GenericTrigger> queryInformationHolder = new QueryInformationHolder<>(
				filter, GenericTrigger.class, securityContext);
		return getAllFiltered(queryInformationHolder, preds, cb, q, r);
	}

	private void addGenericTriggerPredicate(List<Predicate> preds,
			Root<GenericTrigger> r, CriteriaQuery<?> q, CriteriaBuilder cb,
			GenericTriggerFilter filter, SecurityContext securityContext) {

	}

	public long countAllGenericTriggers(GenericTriggerFilter filter,
			SecurityContext securityContext) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> q = cb.createQuery(Long.class);
		Root<GenericTrigger> r = q.from(GenericTrigger.class);
		List<Predicate> preds = new ArrayList<>();
		addGenericTriggerPredicate(preds, r, q, cb, filter, securityContext);
		QueryInformationHolder<GenericTrigger> queryInformationHolder = new QueryInformationHolder<>(
				filter, GenericTrigger.class, securityContext);
		return countAllFiltered(queryInformationHolder, preds, cb, q, r);
	}
}
