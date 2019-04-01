package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.FlexiCoreRule;
import com.flexicore.rules.model.FlexiCoreRuleArgument;
import com.flexicore.rules.request.RuleArgumentFilter;
import com.flexicore.rules.request.RulesFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@PluginInfo(version = 1)
public class RuleArgumentRepository extends AbstractRepositoryPlugin {

    public List<FlexiCoreRuleArgument> listAllRuleArguments(RuleArgumentFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<FlexiCoreRuleArgument> q=cb.createQuery(FlexiCoreRuleArgument.class);
        Root<FlexiCoreRuleArgument> r=q.from(FlexiCoreRuleArgument.class);
        List<Predicate> preds=new ArrayList<>();
        addRuleArgumentsPredicate(preds,r,cb,filter);
        QueryInformationHolder<FlexiCoreRuleArgument> queryInformationHolder=new QueryInformationHolder<>(filter,FlexiCoreRuleArgument.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    private void addRuleArgumentsPredicate(List<Predicate> preds, Root<FlexiCoreRuleArgument> r, CriteriaBuilder cb, RuleArgumentFilter filter) {


    }

    public long countAllRuleArguments(RuleArgumentFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<FlexiCoreRuleArgument> r=q.from(FlexiCoreRuleArgument.class);
        List<Predicate> preds=new ArrayList<>();
        addRuleArgumentsPredicate(preds,r,cb,filter);
        QueryInformationHolder<FlexiCoreRuleArgument> queryInformationHolder=new QueryInformationHolder<>(filter,FlexiCoreRuleArgument.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }
}
