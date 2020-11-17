package com.flexicore.rules.repository;

import com.flexicore.annotations.plugins.PluginInfo;
import com.flexicore.interfaces.AbstractRepositoryPlugin;
import com.flexicore.model.QueryInformationHolder;
import com.flexicore.rules.model.*;
import com.flexicore.rules.request.ScenarioFilter;
import com.flexicore.rules.request.JSFunctionFilter;
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
public class JSFunctionRepository extends AbstractRepositoryPlugin {

    public List<JSFunction> listAllJSFunctions(
            JSFunctionFilter filter, SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<JSFunction> q = cb
                .createQuery(JSFunction.class);
        Root<JSFunction> r = q.from(JSFunction.class);
        List<Predicate> preds = new ArrayList<>();
        addJSFunctionPredicate(preds, r, q, cb, filter, securityContext);
        QueryInformationHolder<JSFunction> queryInformationHolder = new QueryInformationHolder<>(
                filter, JSFunction.class, securityContext);
        return getAllFiltered(queryInformationHolder, preds, cb, q, r);
    }

    private void addJSFunctionPredicate(List<Predicate> preds,
                                             Root<JSFunction> r, CriteriaQuery<?> q, CriteriaBuilder cb,
                                             JSFunctionFilter filter, SecurityContext securityContext) {

    }

    public long countAllJSFunctions(JSFunctionFilter filter,
                                         SecurityContext securityContext) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> q = cb.createQuery(Long.class);
        Root<JSFunction> r = q.from(JSFunction.class);
        List<Predicate> preds = new ArrayList<>();
        addJSFunctionPredicate(preds, r, q, cb, filter, securityContext);
        QueryInformationHolder<JSFunction> queryInformationHolder = new QueryInformationHolder<>(
                filter, JSFunction.class, securityContext);
        return countAllFiltered(queryInformationHolder, preds, cb, q, r);
    }
}
