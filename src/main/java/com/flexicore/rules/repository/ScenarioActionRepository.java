package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioActionFilter;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioActionRepository extends AbstractRepositoryPlugin {

    public List<ScenarioAction> listAllScenarioActions(ScenarioActionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<ScenarioAction> q=cb.createQuery(ScenarioAction.class);
        Root<ScenarioAction> r=q.from(ScenarioAction.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioActionPredicate(preds,r,q,cb,filter,securityContext);
        QueryInformationHolder<ScenarioAction> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioAction.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addScenarioActionPredicate(List<Predicate> preds, Root<ScenarioAction> r, CriteriaQuery<?> q, CriteriaBuilder cb, ScenarioActionFilter filter, SecurityContext securityContext) {
        ScenarioFilter scenarioFilter = filter.getScenarioFilter();
        if (scenarioFilter != null) {
            Subquery<String> sub = getConnectedScenarioActionsSubQuery(q, cb, securityContext, scenarioFilter);
            preds.add(filter.isConnected()?r.get(ScenarioAction_.id).in(sub):cb.not(r.get(ScenarioAction_.id).in(sub)));
        }

    }


    private Subquery<String> getConnectedScenarioActionsSubQuery(CriteriaQuery<?> q, CriteriaBuilder cb, SecurityContext securityContext, ScenarioFilter scenarioFilter) {
        Subquery<String> sub = q.subquery(String.class);
        Root<Scenario> sr = sub.from(Scenario.class);
        List<Predicate> subPreds = new ArrayList<>();
        ScenarioRepository.addScenarioPredicate(subPreds, sr, cb, scenarioFilter);
        QueryInformationHolder<Scenario> queryInformationHolder = new QueryInformationHolder<>(scenarioFilter, Scenario.class, securityContext);
        prepareQuery(queryInformationHolder, subPreds, cb, sub, sr);
        Join<Scenario, ScenarioToAction> join = sr.join(Scenario_.scenarioToActions);
        Join<ScenarioToAction, ScenarioAction> join2 = join.join(ScenarioToAction_.scenarioAction);
        subPreds.add(cb.isFalse(join.get(ScenarioToAction_.softDelete)));

        Predicate[] subPredsArr = new Predicate[subPreds.size()];
        subPreds.toArray(subPredsArr);
        sub.select(join2.get(ScenarioTrigger_.id)).where(subPredsArr);
        return sub;
    }

    public long countAllScenarioActions(ScenarioActionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<ScenarioAction> r=q.from(ScenarioAction.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioActionPredicate(preds,r, q, cb,filter, securityContext);
        QueryInformationHolder<ScenarioAction> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioAction.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
