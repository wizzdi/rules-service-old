package com.flexicore.rules.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.flexicore.model.FilteringInformationHolder;
import com.flexicore.rules.model.JSFunction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JSFunctionParameterFilter extends FilteringInformationHolder {

    private Set<String> jsFunctionsIds=new HashSet<>();
    @JsonIgnore
    private List<JSFunction> jsFunctions;

    public Set<String> getJsFunctionsIds() {
        return jsFunctionsIds;
    }

    public <T extends JSFunctionParameterFilter> T setJsFunctionsIds(Set<String> jsFunctionsIds) {
        this.jsFunctionsIds = jsFunctionsIds;
        return (T) this;
    }

    @JsonIgnore
    public List<JSFunction> getJsFunctions() {
        return jsFunctions;
    }

    public <T extends JSFunctionParameterFilter> T setJsFunctions(List<JSFunction> jsFunctions) {
        this.jsFunctions = jsFunctions;
        return (T) this;
    }
}
