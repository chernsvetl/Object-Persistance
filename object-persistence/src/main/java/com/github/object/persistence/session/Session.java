package com.github.object.persistence.session;

import com.github.object.persistence.criteria.Query;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface Session extends AutoCloseable {

    <T> CompletableFuture<Boolean> createTable(Class<T> entityClass);

    /**
     * Достать сущность из хранилища по id.
     *
     * @param entityClass класс сущности
     * @param id          идентификатор
     * @return сущность, находящаяся в хранилище с данным идентификатором
     */
    <T, R> CompletableFuture<T> getRecord(Class<T> entityClass, R id);

    /**
     * Сохранить или обновить сущность в хранилище.
     *
     * @param entity сущность, которую необходимо сохранить
     * @return идентификатор, с котором была сохранена сущность
     */
    <T> CompletableFuture<Boolean> saveOrUpdate(T entity);

    <T> CompletableFuture<Boolean> saveOrUpdate(Collection<T> entities);

    /**
     * Удалить сущность из хранилища.
     *
     * @param entity сущность, которую необходимо сохранить.
     */
    <T> CompletableFuture<Void> deleteRecord(T entity);

    /**
     * Предоставляет конфигуратор запросов по типу сущности.
     *
     * @param clazz тип сущности.
     * @return конфигуратор запросов.
     */
    <T> Query<T> buildQuery(Class<T> clazz);

}
