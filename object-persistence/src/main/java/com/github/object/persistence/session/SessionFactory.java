package com.github.object.persistence.session;

/**
 * Общий интерфейс для фабрики сессий.
 */
public interface SessionFactory {

    /**
     * Создание сессии с подключением к datasource.
     *
     * @return сессия подключения
     */
    Session openSession();

    void initializeDatasource();

    void shutdown();
}
