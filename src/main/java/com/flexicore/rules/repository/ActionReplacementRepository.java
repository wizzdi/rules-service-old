package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ActionReplacementFilter;
import com.flexicore.rules.request.ScenarioFilter;
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
public class ActionReplacementRepository extends AbstractRepositoryPlugin {

    public List<ActionReplacement> listAllActionReplacements(
            ActionReplacementFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ActionReplacement> q = cb.createQuery(ActionReplacement.class);
        Root<ActionReplacement> r = q.from(ActionReplacement.class);
        List<Predicate> preds = new ArrayList<>();
        addActionReplacementPredicate(preds, r, q, cb, filter, securityContext);
        QueryInformationHolder<ActionReplacement> queryInformationHolder = new QueryInformationHolder<>(filter, ActionReplacement.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    private void addActionReplacementPredicate(List<Predicate> preds,
                                               Root<ActionReplacement> r, CriteriaQuery<?> q, CriteriaBuilder cb,
                                               ActionReplacementFilter filter, SecurityContext securityContext) {
        List<ScenarioToAction> scenarioToActions = filter.getScenarioToActions();
        if (scenarioToActions != null && !scenarioToActions.isEmpty()) {
            Set<String> ids = scenarioToActions.stream().map(f -> f.getId()).collect(Collectors.toSet());
            Join<ActionReplacement, ScenarioToAction> join = r.join(ActionReplacement_.scenarioToAction);
            preds.add(join.get(ScenarioToAction_.id).in(ids));
        }

		List<ScenarioTrigger> scenarioTriggers = filter.getScenarioTriggers();
		if (scenarioTriggers != null && !scenarioTriggers.isEmpty()) {
			Set<String> ids = scenarioTriggers.stream().map(f -> f.getId()).collect(Collectors.toSet());
			Join<ActionReplacement, ScenarioTrigger> join = r.join(ActionReplacement_.scenarioTrigger);
			preds.add(join.get(ScenarioTrigger_.id).in(ids));
		}

    }

    public long countAllActionReplacements(ActionReplacementFilter filter,
                                           SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<ActionReplacement> r = q.from(ActionReplacement.class);
        List<Predicate> preds = new ArrayList<>();
        addActionReplacementPredicate(preds, r, q, cb, filter, securityContext);
        QueryInformationHolder<ActionReplacement> queryInformationHolder = new QueryInformationHolder<>(
                filter, ActionReplacement.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }
}
