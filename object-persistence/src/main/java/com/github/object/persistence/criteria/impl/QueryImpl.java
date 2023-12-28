package com.github.object.persistence.criteria.impl;

import com.github.object.persistence.criteria.Predicate;
import com.github.object.persistence.criteria.Query;
import com.github.object.persistence.session.AbstractSession;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public final class QueryImpl<T> implements Query<T> {

    private final AbstractSession session;
    private final Class<T> clazz;

    private QueryImpl(AbstractSession session, Class<T> clazz) {
        this.session = session;
        this.clazz = clazz;
    }

    public static <T> Query<T> getQuery(AbstractSession session, Class<T> clazz) {
        return new QueryImpl<>(session, clazz);
    }

    @Override
    public CompletableFuture<List<T>> selectWhere(Predicate predicate) {
        return session.getRecords(clazz, getPredicate(predicate));
    }

    @Override
    public CompletableFuture<Long> updateWhere(Map<String, Object> fieldValueMap, Predicate predicate) {
        Map<String, Object> map = validateFieldValueMap(fieldValueMap);
        return session.updateRecord(clazz, map, getPredicate(predicate));
    }

    @Override
    public CompletableFuture<Void> deleteWhere(Predicate predicate) {
        return session.deleteRecord(clazz, getPredicate(predicate));
    }

    private Optional<String> getPredicate(Predicate predicate) {
        if (Objects.isNull(predicate) || predicate.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(predicate.toString());
    }

    private Map<String, Object> validateFieldValueMap(Map<String, Object> map) {
        if (Objects.isNull(map)) {
            throw new IllegalArgumentException("Значение 'fieldValueMap' не должно быть null");
        }

        Map<String, Object> fieldValueMap = new HashMap<>(map);
        fieldValueMap.remove(null);
        if (fieldValueMap.isEmpty()) {
            throw new IllegalArgumentException("Значение 'fieldValueMap' не должно быть пустым");
        }

        return fieldValueMap;
    }

}
