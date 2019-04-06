package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.ScenarioToTrigger;
import com.flexicore.rules.request.ScenarioToTriggerFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
