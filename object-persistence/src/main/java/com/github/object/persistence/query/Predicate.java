package com.github.object.persistence.query;

import java.util.Set;

public interface Predicate {

    PredicateBuilder builder();

    Set<String> usedVariables();

    boolean isEmpty();

}
