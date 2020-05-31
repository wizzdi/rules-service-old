package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
public class ScenarioToTriggerRepository extends AbstractRepositoryPlugin {

    public List<ScenarioToTrigger> listAllScenarioToTriggers(ScenarioToTriggerFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<ScenarioToTrigger> q=cb.createQuery(ScenarioToTrigger.class);
        Root<ScenarioToTrigger> r=q.from(ScenarioToTrigger.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioToTriggerPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioToTrigger> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioToTrigger.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addScenarioToTriggerPredicate(List<Predicate> preds, Root<ScenarioToTrigger> r, CriteriaBuilder cb, ScenarioToTriggerFilter filter) {
        Join<ScenarioToTrigger, Scenario> scenarioJoin=null;
        if(filter.getScenarioTriggers()!=null && !filter.getScenarioTriggers().isEmpty()){
            Set<String> ids=filter.getScenarioTriggers().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<ScenarioToTrigger, ScenarioTrigger> join=r.join(ScenarioToTrigger_.scenarioTrigger);
            preds.add(join.get(ScenarioTrigger_.id).in(ids));
        }

        if(filter.getScenarios()!=null && !filter.getScenarios().isEmpty()){
            Set<String> ids=filter.getScenarios().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
             scenarioJoin=scenarioJoin==null?r.join(ScenarioToTrigger_.scenario):scenarioJoin;
            preds.add(scenarioJoin.get(Scenario_.id).in(ids));
        }

        if(filter.getEnabled()!=null){
            preds.add(cb.equal(r.get(ScenarioToTrigger_.enabled),filter.getEnabled()));
        }
        if(filter.getNonDeletedScenarios()!=null &&filter.getNonDeletedScenarios()){
            scenarioJoin=scenarioJoin==null?r.join(ScenarioToTrigger_.scenario):scenarioJoin;
            preds.add(cb.isFalse(scenarioJoin.get(Scenario_.softDelete)));
        }
    }

    public long countAllScenarioToTriggers(ScenarioToTriggerFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<ScenarioToTrigger> r=q.from(ScenarioToTrigger.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioToTriggerPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioToTrigger> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioToTrigger.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
