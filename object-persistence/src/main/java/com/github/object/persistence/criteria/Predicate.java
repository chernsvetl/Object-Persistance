package com.github.object.persistence.criteria;

import java.util.Set;

public interface Predicate {

    PredicateBuilder builder();

    Set<String> usedVariables();

    boolean isEmpty();

}
