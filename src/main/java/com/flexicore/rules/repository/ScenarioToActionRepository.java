package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioToActionFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
public class ScenarioToActionRepository extends AbstractRepositoryPlugin {

    public List<ScenarioToAction> listAllScenarioToActions(ScenarioToActionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<ScenarioToAction> q=cb.createQuery(ScenarioToAction.class);
        Root<ScenarioToAction> r=q.from(ScenarioToAction.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioToActionPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioToAction> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioToAction.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addScenarioToActionPredicate(List<Predicate> preds, Root<ScenarioToAction> r, CriteriaBuilder cb, ScenarioToActionFilter filter) {

        if(filter.getScenarios()!=null && !filter.getScenarios().isEmpty()){
            Set<String> ids=filter.getScenarios().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<ScenarioToAction, Scenario> join=r.join(ScenarioToAction_.scenario);
            preds.add(join.get(Scenario_.id).in(ids));
        }

        if(filter.getScenarioActions()!=null && !filter.getScenarioActions().isEmpty()){
            Set<String> ids=filter.getScenarioActions().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<ScenarioToAction, ScenarioAction> join=r.join(ScenarioToAction_.scenarioAction);
            preds.add(join.get(ScenarioToAction_.id).in(ids));
        }
        if(filter.getEnabled()!=null){
            preds.add(cb.equal(r.get(ScenarioToAction_.enabled),filter.getEnabled()));
        }

    }

    public long countAllScenarioToActions(ScenarioToActionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<ScenarioToAction> r=q.from(ScenarioToAction.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioToActionPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioToAction> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioToAction.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
