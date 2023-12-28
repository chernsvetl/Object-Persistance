package com.github.object.persistence.criteria;

import java.util.Set;

public interface PredicateBuilder {

    PredicateBuilder and(PredicateBuilder other);

    PredicateBuilder or(PredicateBuilder other);

    PredicateBuilder not();

    PredicateBuilder isNull(String field);

    PredicateBuilder isNotNull(String field);

    PredicateBuilder equal(String field, Object value);

    PredicateBuilder notEqual(String field, Object value);

    PredicateBuilder greaterThan(String field, Object value);

    PredicateBuilder greaterThanOrEqual(String field, Object value);

    PredicateBuilder lessThan(String field, Object value);

    PredicateBuilder lessThanOrEqual(String field, Object value);

    PredicateBuilder between(String field, Object leftBound, Object rightBound);

    PredicateBuilder in(String field, Set<Object> set);

    Set<String> usedVariables();

    boolean isEmpty();

    void clear();

    Predicate build();

}
