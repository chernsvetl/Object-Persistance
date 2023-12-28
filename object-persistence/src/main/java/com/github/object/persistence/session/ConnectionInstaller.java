package com.github.object.persistence.session;

/**
 * установщик соединения
 *
 * @param <T> тип датасурса
 */
public interface ConnectionInstaller<T> {

    DataSourceWrapper<T> installConnection();
}
