package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.ScenarioToAction;
import com.flexicore.rules.request.ScenarioToActionFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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
