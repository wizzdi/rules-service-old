package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.RuleLinkFilter;
import com.flexicore.rules.request.RulesFilter;
import com.flexicore.security.SecurityContext;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void massMerge(List<?> toMerge) {
        super.massMerge(toMerge);
    }

    public static void addRulesPredicate(List<Predicate> preds, Root<FlexiCoreRule> r, CriteriaBuilder cb, RulesFilter filter) {


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

    public List<FlexiCoreRuleLink> listAllRuleLinks(RuleLinkFilter ruleLinkFilter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<FlexiCoreRuleLink> q=cb.createQuery(FlexiCoreRuleLink.class);
        Root<FlexiCoreRuleLink> r=q.from(FlexiCoreRuleLink.class);
        List<Predicate> preds=new ArrayList<>();
        addRulesLinkPredicate(preds,r,cb,ruleLinkFilter);
        QueryInformationHolder<FlexiCoreRuleLink> queryInformationHolder=new QueryInformationHolder<>(ruleLinkFilter,FlexiCoreRuleLink.class,securityContext);
        return getAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    public long countAllRuleLinks(RuleLinkFilter ruleLinkFilter, SecurityContext securityContext) {
        CriteriaBuilder cb=em.getCriteriaBuilder();
        CriteriaQuery<Long> q=cb.createQuery(Long.class);
        Root<FlexiCoreRuleLink> r=q.from(FlexiCoreRuleLink.class);
        List<Predicate> preds=new ArrayList<>();
        addRulesLinkPredicate(preds,r,cb,ruleLinkFilter);
        QueryInformationHolder<FlexiCoreRuleLink> queryInformationHolder=new QueryInformationHolder<>(ruleLinkFilter,FlexiCoreRuleLink.class,securityContext);
        return countAllFiltered(queryInformationHolder,preds,cb,q,r);
    }

    public static void addRulesLinkPredicate(List<Predicate> preds, Root<FlexiCoreRuleLink> r, CriteriaBuilder cb, RuleLinkFilter ruleLinkFilter) {
        if(ruleLinkFilter.getFlexiCoreRuleOps()!=null && !ruleLinkFilter.getFlexiCoreRuleOps().isEmpty()){
            Set<String> ids=ruleLinkFilter.getFlexiCoreRuleOps().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<FlexiCoreRuleLink, FlexiCoreRuleOp> join=r.join(FlexiCoreRuleLink_.ruleOp);
            preds.add(join.get(FlexiCoreRuleOp_.id).in(ids));
        }

        if(ruleLinkFilter.getFlexiCoreRules()!=null && !ruleLinkFilter.getFlexiCoreRules().isEmpty()){
            Set<String> ids=ruleLinkFilter.getFlexiCoreRules().parallelStream().map(f->f.getId()).collect(Collectors.toSet());
            Join<FlexiCoreRuleLink, FlexiCoreRule> join=r.join(FlexiCoreRuleLink_.ruleToEval);
            preds.add(join.get(FlexiCoreRule_.id).in(ids));
        }

    }
}
