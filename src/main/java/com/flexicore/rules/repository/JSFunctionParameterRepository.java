package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.JSFunctionParameterFilter;
import com.flexicore.security.SecurityContext;
import org.pf4j.Extension;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PluginInfo(version = 1)
@Extension
@Component
public class JSFunctionParameterRepository extends AbstractRepositoryPlugin {

    public List<JSFunctionParameter> listAllJSFunctionParameters(
            JSFunctionParameterFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JSFunctionParameter> q = cb
                .createQuery(JSFunctionParameter.class);
        Root<JSFunctionParameter> r = q.from(JSFunctionParameter.class);
        List<Predicate> preds = new ArrayList<>();
        addJSFunctionParameterPredicate(preds, r, q, cb, filter, securityContext);
        QueryInformationHolder<JSFunctionParameter> queryInformationHolder = new QueryInformationHolder<>(
                filter, JSFunctionParameter.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    private void addJSFunctionParameterPredicate(List<Predicate> preds,
                                             Root<JSFunctionParameter> r, CriteriaQuery<?> q, CriteriaBuilder cb,
                                             JSFunctionParameterFilter filter, SecurityContext securityContext) {


        if (filter.getJsFunctions() != null && !filter.getJsFunctions().isEmpty()) {
            Set<String> ids=filter.getJsFunctions().stream().map(f->f.getId()).collect(Collectors.toSet());
            Join<JSFunctionParameter,JSFunction> join=r.join(JSFunctionParameter_.jsFunction);
            preds.add(join.get(JSFunction_.id).in(ids));
        }
    }


    public long countAllJSFunctionParameters(JSFunctionParameterFilter filter,
                                         SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<JSFunctionParameter> r = q.from(JSFunctionParameter.class);
        List<Predicate> preds = new ArrayList<>();
        addJSFunctionParameterPredicate(preds, r, q, cb, filter, securityContext);
        QueryInformationHolder<JSFunctionParameter> queryInformationHolder = new QueryInformationHolder<>(
                filter, JSFunctionParameter.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }
}
