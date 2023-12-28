package com.github.object.persistence.session.impl;

import com.github.object.persistence.config.ConfigDataSource;
import com.github.object.persistence.session.ConnectionInstaller;
import com.github.object.persistence.session.DataSourceWrapper;
import com.github.object.persistence.session.Session;
import com.github.object.persistence.session.SessionFactory;
import org.atteo.classindex.ClassIndex;

import javax.persistence.Entity;
import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class SqlFactoryImpl implements SessionFactory {

    private final ConnectionInstaller<Connection> installer;
    private final ThreadPoolExecutor executor;

    public SqlFactoryImpl(ConnectionInstaller<Connection> installer) {
        this.installer = installer;
        initializeDatasource();
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(ConfigDataSource.getInstance().getThreadPoolSize());
    }

    @Override
    public Session openSession() {
        DataSourceWrapper<Connection> wrapper = installer.installConnection();
        FromSqlToObjectMapper<Connection> mapper = new FromSqlToObjectMapper<>(SqlGenerator.getInstance());
        return new SqlSession(wrapper, mapper, executor);
    }

    @Override
    public void initializeDatasource() {
        if (ConfigDataSource.getInstance().isInitializeNeeded()) {
            Iterable<Class<?>> entityClasses = ClassIndex.getAnnotated(Entity.class);
            installer.installConnection().execute(validateAndCreateTables(entityClasses));
        }
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    private String validateAndCreateTables(Iterable<Class<?>> entityClasses) {
        return StreamSupport
                .stream(entityClasses.spliterator(), false)
                .map(kClass -> SqlGenerator.getInstance().createTable(kClass))
                .collect(Collectors.joining(" "));
    }
}
