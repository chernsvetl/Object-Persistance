package com.github.object.persistence.session.impl;

import com.github.object.persistence.session.AbstractSession;
import com.github.object.persistence.session.DataSourceWrapper;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class SqlSession extends AbstractSession {

    private final DataSourceWrapper<Connection> connection;
    private final FromSqlToObjectMapper<Connection> mapper;


    public SqlSession(DataSourceWrapper<Connection> connection, FromSqlToObjectMapper<Connection> mapper, ExecutorService executor) {
        super(executor);
        this.connection = connection;
        this.mapper = mapper;
    }

    @Override
    public <T> CompletableFuture<Boolean> createTable(Class<T> entityClass) {
        return CompletableFuture.supplyAsync(() -> mapper.createTable(connection, entityClass), executor);
    }

    @Override
    public <T, R> CompletableFuture<T> getRecord(Class<T> entityClass, R id) {
        return CompletableFuture.supplyAsync(() -> mapper.get(connection, entityClass, id), executor);
    }

    public <T> CompletableFuture<List<T>> getRecords(Class<T> entityClass, Optional<String> predicate) {
        return CompletableFuture.supplyAsync(() -> mapper.get(connection, entityClass, predicate), executor);
    }

    @Override
    public <T> CompletableFuture<Boolean> saveOrUpdate(T entity) {
        if (mapper.isEntityExistInDB(connection, entity)) {
            return CompletableFuture.supplyAsync(() -> mapper.update(connection, entity) == 1, executor);
        }
        return CompletableFuture.supplyAsync(() -> mapper.insert(connection, entity) == 1, executor);
    }

    @Override
    public <T> CompletableFuture<Boolean> saveOrUpdate(Collection<T> entities) {
        return CompletableFuture.supplyAsync(() -> mapper.insert(connection, entities) == entities.size(), executor);
    }

    @Override
    public <T> CompletableFuture<Long> updateRecord(Class<T> entityClass, Map<String, Object> fieldValueMap, Optional<String> predicate) {
        return CompletableFuture.supplyAsync(() -> mapper.update(connection, entityClass, fieldValueMap, predicate), executor);
    }

    @Override
    public <T> CompletableFuture<Void> deleteRecord(T entity) {
        return CompletableFuture.runAsync(() -> mapper.delete(connection, entity), executor);
    }

    public <T> CompletableFuture<Void> deleteRecord(Class<T> entityClass, Optional<String> predicate) {
        return CompletableFuture.runAsync(() -> mapper.delete(connection, entityClass, predicate), executor);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
