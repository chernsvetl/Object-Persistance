package com.github.object.persistence.persistence.sql.impl.criteria;

import java.util.Set;

import com.github.object.persistence.query.Predicate;
import com.github.object.persistence.query.PredicateBuilder;


public final class PredicateSqlImpl implements Predicate {

    private final String predicate;
    private final Set<String> usedVariables;

    PredicateSqlImpl(String predicate, Set<String> usedVariables) {
        this.predicate = predicate;
        this.usedVariables = Set.copyOf(usedVariables);
    }

    @Override
    public PredicateBuilder builder() {
        return new PredicateBuilderSqlImpl(this);
    }

    @Override
    public Set<String> usedVariables() {
        return usedVariables;
    }

    @Override
    public boolean isEmpty() {
        return predicate.isBlank();
    }

    @Override
    public String toString() {
        return predicate;
    }
}
