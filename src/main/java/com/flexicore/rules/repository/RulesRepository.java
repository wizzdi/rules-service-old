package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.request.RulesFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class RulesRepository extends AbstractRepositoryPlugin {

    public List<FlexiCoreRule> listAllRules(RulesFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<FlexiCoreRule> q=cb.createQuery(FlexiCoreRule.class);
        Root<FlexiCoreRule> r=q.from(FlexiCoreRule.class);
        List<Predicate> preds=new ArrayList<>();
        addRulesPredicate(preds,r,cb,filter);
        QueryInformationHolder<FlexiCoreRule> queryInformationHolder=new QueryInformationHolder<>(filter,FlexiCoreRule.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addRulesPredicate(List<Predicate> preds, Root<FlexiCoreRule> r, CriteriaBuilder cb, RulesFilter filter) {


    }

    public long countAllRules(RulesFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<FlexiCoreRule> r=q.from(FlexiCoreRule.class);
        List<Predicate> preds=new ArrayList<>();
        addRulesPredicate(preds,r,cb,filter);
        QueryInformationHolder<FlexiCoreRule> queryInformationHolder=new QueryInformationHolder<>(filter,FlexiCoreRule.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
