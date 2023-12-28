package com.github.object.persistence.session;

import com.github.object.persistence.query.Query;
import com.github.object.persistence.query.impl.QueryImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public abstract class AbstractSession implements Session {
    protected final ExecutorService executor;

    protected AbstractSession(ExecutorService executor) {
        this.executor = executor;
    }

    public abstract <T> CompletableFuture<List<T>> getRecords(Class<T> entityClass, Optional<String> predicate);

    public abstract <T> CompletableFuture<Long> updateRecord(Class<T> entityClass, Map<String, Object> fieldValueMap, Optional<String> predicate);

    public abstract <T> CompletableFuture<Void> deleteRecord(Class<T> entityClass, Optional<String> predicate);

    @Override
    public <T> Query<T> buildQuery(Class<T> clazz) {
        return QueryImpl.getQuery(this, clazz);
    }

}
