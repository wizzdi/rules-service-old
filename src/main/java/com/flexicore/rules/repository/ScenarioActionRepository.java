package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.ScenarioAction;
import com.flexicore.rules.request.ScenarioActionFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class ScenarioActionRepository extends AbstractRepositoryPlugin {

    public List<ScenarioAction> listAllScenarioActions(ScenarioActionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<ScenarioAction> q=cb.createQuery(ScenarioAction.class);
        Root<ScenarioAction> r=q.from(ScenarioAction.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioActionPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioAction> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioAction.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addScenarioActionPredicate(List<Predicate> preds, Root<ScenarioAction> r, CriteriaBuilder cb, ScenarioActionFilter filter) {


    }

    public long countAllScenarioActions(ScenarioActionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<ScenarioAction> r=q.from(ScenarioAction.class);
        List<Predicate> preds=new ArrayList<>();
        addScenarioActionPredicate(preds,r,cb,filter);
        QueryInformationHolder<ScenarioAction> queryInformationHolder=new QueryInformationHolder<>(filter,ScenarioAction.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
