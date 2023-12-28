package com.github.object.persistence.persistence.sql.impl.criteria;

import java.util.Set;

import com.google.common.collect.Sets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.object.persistence.criteria.Predicate;
import com.github.object.persistence.criteria.PredicateBuilder;

/**
 * Тестовые сценарии проверки класса {@link PredicateBuilderSqlImpl}.
 */
class PredicateBuilderSqlImplTest {

    @Test
    void builder_returnBuilderWithDefaultValues() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder();

        Assertions.assertTrue(builder.usedVariables().isEmpty());
        Assertions.assertTrue(builder.isEmpty());
    }

    @Test
    void and_actualPredicateAndAnotherPredicateIsEmpty_returnEmptyPredicate() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder();
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder();

        PredicateBuilder combination = builder1.and(builder2);

        Assertions.assertEquals(combination.toString(), builder2.toString());
        Assertions.assertEquals(combination.usedVariables(), builder2.usedVariables());
    }

    @Test
    void and_actualPredicateIsEmptyAnotherPredicateIsNotEmpty_returnAnotherPredicate() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder();
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder()
                .between("var", 0, 10);

        PredicateBuilder combination = builder1.and(builder2);

        Assertions.assertEquals(combination.toString(), builder2.toString());
        Assertions.assertEquals(combination.usedVariables(), builder2.usedVariables());
    }

    @Test
    void and_actualPredicateIsNotEmptyAnotherPredicateIsEmpty_returnAnotherPredicate() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder()
                .between("var", 0, 10);
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder();

        PredicateBuilder combination = builder1.and(builder2);

        Assertions.assertEquals(combination.toString(), builder1.toString());
        Assertions.assertEquals(combination.usedVariables(), builder1.usedVariables());
    }

    @Test
    void and_actualPredicateAndAnotherPredicateIsNotEmpty_returnANDCombinationOfThem() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder()
                .isNotNull("var1");
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder()
                .between("var2", 0, 10);
        String expectedPredicate = "(var1 IS NOT NULL) AND (var2 BETWEEN '0' AND '10')";
        Set<String> expectedVariables = Set.of("var1", "var2");

        PredicateBuilder combination = builder1.and(builder2);

        Assertions.assertEquals(expectedPredicate, combination.toString());
        Assertions.assertEquals(expectedVariables, combination.usedVariables());
    }

    @Test
    void or_actualPredicateAndAnotherPredicateIsEmpty_returnEmptyPredicate() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder();
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder();

        PredicateBuilder combination = builder1.or(builder2);

        Assertions.assertEquals(combination.toString(), builder2.toString());
        Assertions.assertEquals(combination.usedVariables(), builder2.usedVariables());
    }

    @Test
    void or_actualPredicateIsEmptyAnotherPredicateIsNotEmpty_returnAnotherPredicate() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder();
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder()
                .between("var", 0, 10);

        PredicateBuilder combination = builder1.or(builder2);

        Assertions.assertEquals(combination.toString(), builder2.toString());
        Assertions.assertEquals(combination.usedVariables(), builder2.usedVariables());
    }

    @Test
    void or_actualPredicateIsNotEmptyAnotherPredicateIsEmpty_returnAnotherPredicate() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder()
                .between("var", 0, 10);
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder();

        PredicateBuilder combination = builder1.or(builder2);

        Assertions.assertEquals(combination.toString(), builder1.toString());
        Assertions.assertEquals(combination.usedVariables(), builder1.usedVariables());
    }

    @Test
    void or_actualPredicateAndAnotherPredicateIsNotEmpty_returnANDCombinationOfThem() {
        PredicateBuilder builder1 = PredicateBuilderSqlImpl.builder()
                .isNotNull("var1");
        PredicateBuilder builder2 = PredicateBuilderSqlImpl.builder()
                .between("var2", 0, 10);
        String expectedPredicate = "(var1 IS NOT NULL) OR (var2 BETWEEN '0' AND '10')";
        Set<String> expectedVariables = Set.of("var1", "var2");

        PredicateBuilder combination = builder1.or(builder2);

        Assertions.assertEquals(expectedPredicate, combination.toString());
        Assertions.assertEquals(expectedVariables, combination.usedVariables());
    }

    @Test
    void not_actualPredicateIsNotEmpty_returnNegationOfActualPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .between("var1", 0, 10)
                .not();
        String expectedPredicate = "NOT (var1 BETWEEN '0' AND '10')";
        Set<String> expectedVariables = Set.of("var1");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void not_actualPredicateIsEmpty_returnActualEmptyPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .not();
        String expectedPredicate = "";
        Set<String> expectedVariables = Set.of();

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void isNull_fieldIsNotNull_returnPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .isNull("var");
        String expectedPredicate = "var IS NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void isNull_fieldIsNull_throwNPEWithSpecificMessage() {
        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> PredicateBuilderSqlImpl.builder().isNull(null)
        );

        Assertions.assertEquals(
                "Значение поля 'field' не должно быть null",
                exception.getMessage()
        );
    }

    @Test
    void isNotNull_fieldIsNotNull_returnPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .isNotNull("var");
        String expectedPredicate = "var IS NOT NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void equal_valueIsNotNull_returnPredicateWithValueWrappedInQuotes() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", "JPA");
        String expectedPredicate = "var = 'JPA'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void equal_valueIsNull_returnPredicateWithNullValue() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", null);
        String expectedPredicate = "var = NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void notEqual_valueIsNotNull_returnPredicateWithValueWrappedInQuotes() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .notEqual("var", "JPA");
        String expectedPredicate = "var <> 'JPA'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void greaterThan_valueIsNotNull_returnPredicateWithValueWrappedInQuotes() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .greaterThan("var", 123);
        String expectedPredicate = "var > '123'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void greaterThan_valueIsNull_returnPredicateWithNullValue() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .greaterThan("var", null);
        String expectedPredicate = "var > NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void greaterThanOrEqual_valueIsNotNull_returnPredicateWithValueWrappedInQuotes() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .greaterThanOrEqual("var", 123);
        String expectedPredicate = "var >= '123'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void greaterThanOrEqual_valueIsNull_returnPredicateWithNullValue() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .greaterThanOrEqual("var", null);
        String expectedPredicate = "var >= NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void lessThan_valueIsNotNull_returnPredicateWithValueWrappedInQuotes() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .lessThan("var", 123);
        String expectedPredicate = "var < '123'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void lessThan_valueIsNull_returnPredicateWithNullValue() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .lessThan("var", null);
        String expectedPredicate = "var < NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void lessThanOrEqual_valueIsNotNull_returnPredicateWithValueWrappedInQuotes() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .lessThanOrEqual("var", 123);
        String expectedPredicate = "var <= '123'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void lessThanOrEqual_valueIsNull_returnPredicateWithNullValue() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .lessThanOrEqual("var", null);
        String expectedPredicate = "var <= NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void in_containsNull_returnINPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .in("var", Sets.newHashSet(-10, null, 1, 5, null, null, 1, 10));
        String expectedPredicate = "var IN (NULL, '1', '5', '-10', '10')";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void in_setIsNull_returnEmptyPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .in("var", null);
        String expectedPredicate = "";
        Set<String> expectedVariables = Set.of();

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void in_setIsEmpty_returnEmptyPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .in("var", Set.of());
        String expectedPredicate = "";
        Set<String> expectedVariables = Set.of();

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void between_containsNonNull_returnBetweenPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .between("var", -10, 10);
        String expectedPredicate = "var BETWEEN '-10' AND '10'";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void between_containsNull_returnBetweenPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .between("var", null, null);
        String expectedPredicate = "var BETWEEN NULL AND NULL";
        Set<String> expectedVariables = Set.of("var");

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void build_actualPredicateBuilderIsNotEmpty_returnCorrectPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", 12345);
        String expectedPredicate = "var = '12345'";
        Set<String> expectedVariables = Set.of("var");

        Predicate predicate = builder.build();

        Assertions.assertEquals(expectedPredicate, predicate.toString());
        Assertions.assertEquals(expectedVariables, predicate.usedVariables());

        Assertions.assertEquals("", builder.toString());
        Assertions.assertEquals(Set.of(), builder.usedVariables());
    }

    @Test
    void usedVariables_actualPredicateBuilderIsNotEmpty_returnImmutableSetOfVariables() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", 12345);
        Set<String> expectedVariables = Set.of("var");

        Set<String> usedVariables = builder.usedVariables();

        Assertions.assertEquals(expectedVariables, usedVariables);
        Assertions.assertThrows(
                UnsupportedOperationException.class,
                () -> usedVariables.add("123")
        );
    }

    @Test
    void isEmpty_actualPredicateBuilderIsNotEmpty_returnFalse() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", 12345);

        Assertions.assertFalse(builder.isEmpty());
    }

    @Test
    void isEmpty_actualPredicateBuilderIsEmpty_returnTrue() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder();

        Assertions.assertTrue(builder.isEmpty());
    }

    @Test
    void clear_actualPredicateBuilderIsNotEmpty_returnEmptyPredicate() {
        String expectedPredicate = "";
        Set<String> expectedVariables = Set.of();
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", 12345);
        Assertions.assertEquals("var = '12345'", builder.toString());
        Assertions.assertEquals(Set.of("var"), builder.usedVariables());

        builder.clear();

        Assertions.assertEquals(expectedPredicate, builder.toString());
        Assertions.assertEquals(expectedVariables, builder.usedVariables());
    }

    @Test
    void toString_actualPredicateBuilderIsNotEmpty_returnStringPredicate() {
        PredicateBuilder builder = PredicateBuilderSqlImpl.builder()
                .equal("var", 12345);
        String expectedPredicate = "var = '12345'";

        Assertions.assertEquals(expectedPredicate, builder.toString());
    }

    @Test
    void builder_complexTestWithLargePredicate_success() {
        String expectedPredicate = "(((var1 IS NOT NULL) AND (var2 > '10')) AND (var3 IN ('1', '12', '5'))) OR" +
                " ((var4 = '123') AND (NOT ((var5 BETWEEN '-5' AND '5') OR (var6 <= '0'))))";
        Set<String> expectedVariables = Set.of("var1", "var2", "var3", "var4", "var5", "var6");

        Predicate predicate = PredicateBuilderSqlImpl.builder()
                .isNotNull("var1")
                .greaterThan("var2", 10)
                .in("var3", Sets.newHashSet(1, 5, 12))
                .or(PredicateBuilderSqlImpl.builder()
                        .equal("var4", 123)
                        .and(PredicateBuilderSqlImpl.builder()
                                        .between("var5", -5, 5)
                                        .or(PredicateBuilderSqlImpl.builder()
                                                        .lessThanOrEqual("var6", 0)
                                        )
                                        .not()
                        )
                ).build();

        Assertions.assertEquals(expectedVariables, predicate.usedVariables());
        Assertions.assertEquals(expectedPredicate, predicate.toString());
    }
}
