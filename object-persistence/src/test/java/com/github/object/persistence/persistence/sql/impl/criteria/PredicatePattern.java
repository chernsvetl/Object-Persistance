package com.github.object.persistence.persistence.sql.impl.criteria;

import java.util.Locale;

enum PredicatePattern {

    AND("(%s) AND (%s)"),
    OR("(%s) OR (%s)"),
    NOT("NOT (%s)"),
    IS_NULL("%s IS NULL"),
    IS_NOT_NULL("%s IS NOT NULL"),
    IN("%s IN (%s)"),
    BETWEEN("%s BETWEEN %s AND %s"),
    EQUAL("%s = %s"),
    NOT_EQUAL("%s <> %s"),
    LESS_THAN("%s < %s"),
    LESS_THAN_OR_EQUAL("%s <= %s"),
    GREATER_THAN("%s > %s"),
    GREATER_THAN_OR_EQUAL("%s >= %s");

    private final String pattern;

    PredicatePattern(String pattern) {
        this.pattern = pattern;
    }

    String getPattern() {
        return pattern;
    }

    static String resolvePattern(PredicatePattern pattern, Object... values) {
        return String.format(Locale.ENGLISH, pattern.getPattern(), values);
    }
}
