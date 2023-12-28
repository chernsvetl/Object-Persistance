package com.github.object.persistence.persistence.sql.impl.criteria;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.object.persistence.criteria.Predicate;
import com.github.object.persistence.criteria.PredicateBuilder;

public final class PredicateBuilderSqlImpl implements PredicateBuilder {

    private static final PredicatePattern DEFAULT_OPERATOR = PredicatePattern.AND;

    private String predicate;
    private Set<String> usedVariables;

    PredicateBuilderSqlImpl() {
        setDefaultValues();
    }

    PredicateBuilderSqlImpl(Predicate pred) {
        this.predicate = pred.toString();
        this.usedVariables = new HashSet<>(pred.usedVariables());
    }

    public static PredicateBuilder builder() {
        return new PredicateBuilderSqlImpl();
    }

    @Override
    public PredicateBuilder and(PredicateBuilder other) {
        return addOtherPredicateBuilder(PredicatePattern.AND, other);
    }

    @Override
    public PredicateBuilder or(PredicateBuilder other) {
        return addOtherPredicateBuilder(PredicatePattern.OR, other);
    }

    @Override
    public PredicateBuilder not() {
        if (isEmpty()) {
            return this;
        }

        this.predicate = PredicatePattern.resolvePattern(
                PredicatePattern.NOT,
                this
        );
        return this;
    }

    @Override
    public PredicateBuilder isNull(String field) {
        return addOneVariablePredicate(PredicatePattern.IS_NULL, field);
    }

    @Override
    public PredicateBuilder isNotNull(String field) {
        return addOneVariablePredicate(PredicatePattern.IS_NOT_NULL, field);
    }

    @Override
    public PredicateBuilder equal(String field, Object value) {
        return addOneVariablePredicate(PredicatePattern.EQUAL, field, value);
    }

    @Override
    public PredicateBuilder notEqual(String field, Object value) {
        return addOneVariablePredicate(PredicatePattern.NOT_EQUAL, field, value);
    }

    @Override
    public PredicateBuilder greaterThan(String field, Object value) {
        return addOneVariablePredicate(PredicatePattern.GREATER_THAN, field, value);
    }

    @Override
    public PredicateBuilder greaterThanOrEqual(String field, Object value) {
        return addOneVariablePredicate(PredicatePattern.GREATER_THAN_OR_EQUAL, field, value);
    }

    @Override
    public PredicateBuilder lessThan(String field, Object value) {
        return addOneVariablePredicate(PredicatePattern.LESS_THAN, field, value);
    }

    @Override
    public PredicateBuilder lessThanOrEqual(String field, Object value) {
        return addOneVariablePredicate(PredicatePattern.LESS_THAN_OR_EQUAL, field, value);
    }

    @Override
    public PredicateBuilder in(String field, Set<Object> set) {
        if (Objects.isNull(set) || set.isEmpty()) {
            return this;
        }

        String resultSet = set.stream()
                .map(this::stringifyValue)
                .collect(Collectors.joining(", "));

        return addOneVariablePredicate(PredicatePattern.IN, field, resultSet);
    }

    @Override
    public PredicateBuilder between(String field, Object leftBound, Object rightBound) {
        return addOneVariablePredicate(PredicatePattern.BETWEEN, field, leftBound, rightBound);
    }

    @Override
    public Predicate build() {
        Predicate pred = new PredicateSqlImpl(predicate, usedVariables);
        setDefaultValues();
        return pred;
    }

    @Override
    public Set<String> usedVariables() {
        return Set.copyOf(usedVariables);
    }

    @Override
    public boolean isEmpty() {
        return predicate.isBlank();
    }

    @Override
    public void clear() {
        setDefaultValues();
    }

    @Override
    public String toString() {
        return predicate;
    }

    private void setDefaultValues() {
        predicate = "";
        usedVariables = new HashSet<>();
    }

    private String stringifyValue(Object value) {
        if (Objects.isNull(value)) {
            return "NULL";
        }
        return "'" + value + "'";
    }

    private PredicateBuilder addOtherPredicateBuilder(PredicatePattern pattern, PredicateBuilder other) {
        if (this.isEmpty()) {
            return other;
        }
        if (Objects.isNull(other) || other.isEmpty()) {
            return this;
        }

        usedVariables.addAll(other.usedVariables());
        this.predicate = PredicatePattern.resolvePattern(pattern, this, other);
        return this;
    }

    private PredicateBuilder addOneVariablePredicate(PredicatePattern pattern, Object... values) {
        Objects.requireNonNull(values[0],"Значение поля 'field' не должно быть null");

        usedVariables.add(String.valueOf(values[0]));

        if (!Objects.equals(pattern, PredicatePattern.IN)) {
            for (int i = 1; i < values.length; i++) {
                values[i] = stringifyValue(values[i]);
            }
        }

        String predicate = PredicatePattern.resolvePattern(pattern, values);
        buildWithDefaultOperator(predicate);
        return this;
    }

    private void buildWithDefaultOperator(String predicate) {
        if (isEmpty()) {
            this.predicate = predicate;
            return;
        }

        this.predicate = PredicatePattern.resolvePattern(DEFAULT_OPERATOR, this, predicate);
    }

}
