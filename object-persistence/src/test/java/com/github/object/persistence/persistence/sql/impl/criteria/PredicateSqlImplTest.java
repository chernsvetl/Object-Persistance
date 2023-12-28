package com.github.object.persistence.persistence.sql.impl.criteria;

import com.github.object.persistence.criteria.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

/**
 * Тестовые сценарии проверки класса {@link PredicateSqlImpl}.
 */
class PredicateSqlImplTest {

    private static Predicate predicate;

    @BeforeAll
    static void setUp() {
        predicate = new PredicateSqlImpl(
                "(var1 = '5') AND (var2 IN ('1', '7', '9'))",
                Set.of("var1", "var2")
        );
    }

    @Test
    void builder_returnExactlyTheSamePredicate() {
        Predicate actualPredicate = predicate.builder().build();

        Assertions.assertEquals(predicate.toString(), actualPredicate.toString());
        Assertions.assertEquals(predicate.usedVariables(), actualPredicate.usedVariables());
    }

    @Test
    void usedVariables_returnExpectedUsedVariables() {
        Assertions.assertEquals(Set.of("var1", "var2"), predicate.usedVariables());
    }

    @Test
    void toString_returnPredicateValueAsString() {
        Assertions.assertEquals(
                "(var1 = '5') AND (var2 IN ('1', '7', '9'))",
                predicate.toString()
        );
    }

}
