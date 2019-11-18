package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.Scenario;
import com.flexicore.rules.model.ScenarioToTrigger_;
import com.flexicore.rules.model.ScenarioToTrigger;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.model.ScenarioTrigger_;
import com.flexicore.rules.model.Scenario_;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioTriggerRepository extends AbstractRepositoryPlugin {

    public List<ScenarioTrigger> listAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ScenarioTrigger> q = cb.createQuery(ScenarioTrigger.class);
        Root<ScenarioTrigger> r = q.from(ScenarioTrigger.class);
        List<Predicate> preds = new ArrayList<>();
        addScenarioTriggerPredicate(preds, r,q, cb, filter,securityContext);
        QueryInformationHolder<ScenarioTrigger> queryInformationHolder = new QueryInformationHolder<>(filter, ScenarioTrigger.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    private void addScenarioTriggerPredicate(List<Predicate> preds, Root<ScenarioTrigger> r, CriteriaQuery<?> q, CriteriaBuilder cb, ScenarioTriggerFilter filter, SecurityContext securityContext) {
        ScenarioFilter scenarioFilter = filter.getScenarioFilter();
        if (scenarioFilter != null) {
            Subquery<String> sub = getConnectedScenarioTriggersSubQuery(q, cb, securityContext, scenarioFilter);
            preds.add(filter.isConnected()?r.get(ScenarioTrigger_.id).in(sub):cb.not(r.get(ScenarioTrigger_.id).in(sub)));
        }

    }

    private Subquery<String> getConnectedScenarioTriggersSubQuery(CriteriaQuery<?> q, CriteriaBuilder cb, SecurityContext securityContext, ScenarioFilter scenarioFilter) {
        Subquery<String> sub = q.subquery(String.class);
        Root<Scenario> sr = sub.from(Scenario.class);
        List<Predicate> subPreds = new ArrayList<>();
        ScenarioRepository.addScenarioPredicate(subPreds, sr, cb, scenarioFilter);
        QueryInformationHolder<Scenario> queryInformationHolder = new QueryInformationHolder<>(scenarioFilter, Scenario.class, securityContext);
        prepareQuery(queryInformationHolder, subPreds, cb, sub, sr);
        Join<Scenario, ScenarioToTrigger> join = sr.join(Scenario_.scenarioToTriggers);
        Join<ScenarioToTrigger, ScenarioTrigger> join2 = join.join(ScenarioToTrigger_.scenarioTrigger);
        subPreds.add(cb.isFalse(join.get(ScenarioToTrigger_.softDelete)));
        Predicate[] subPredsArr = new Predicate[subPreds.size()];
        subPreds.toArray(subPredsArr);
        sub.select(join2.get(ScenarioTrigger_.id)).where(subPredsArr);
        return sub;
    }

    public long countAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<ScenarioTrigger> r = q.from(ScenarioTrigger.class);
        List<Predicate> preds = new ArrayList<>();
        addScenarioTriggerPredicate(preds, r,q, cb, filter,securityContext);
        QueryInformationHolder<ScenarioTrigger> queryInformationHolder = new QueryInformationHolder<>(filter, ScenarioTrigger.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }
}
