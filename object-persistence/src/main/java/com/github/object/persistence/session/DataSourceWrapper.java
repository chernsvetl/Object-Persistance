package com.github.object.persistence.session;

/**
 * Обертка вокруг dataSource
 * @param <T> тип датасурса
 */
public interface DataSourceWrapper<T> extends AutoCloseable {
    T getSource();

    void execute(String script);
}
