package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.model.ScenarioTrigger;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.rules.request.ScenarioTriggerFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioTriggerRepository extends AbstractRepositoryPlugin {

    public List<ScenarioTrigger> listAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<ScenarioTrigger> q=cb.createQuery(ScenarioTrigger.class);
        Root<ScenarioTrigger> r=q.from(ScenarioTrigger.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioTriggerPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioTrigger> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioTrigger.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addScenarioTriggerPredicate(List<Predicate> preds, Root<ScenarioTrigger> r, CriteriaBuilder cb, ScenarioTriggerFilter filter) {


    }

    public long countAllScenarioTriggers(ScenarioTriggerFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<ScenarioTrigger> r=q.from(ScenarioTrigger.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioTriggerPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioTrigger> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioTrigger.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
