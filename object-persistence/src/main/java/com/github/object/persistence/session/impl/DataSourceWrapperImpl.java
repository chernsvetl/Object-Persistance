package com.github.object.persistence.session.impl;

import com.github.object.persistence.exception.ExecuteException;
import com.github.object.persistence.session.DataSourceWrapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DataSourceWrapperImpl<T extends Connection> implements DataSourceWrapper<T> {

    private final T connection;

    public DataSourceWrapperImpl(T connection) {
        this.connection = connection;
    }

    @Override
    public T getSource() {
        return connection;
    }

    @Override
    public void execute(String script) {
        try (Statement statement = connection.createStatement()) {
            statement.execute(script);
            statement.getResultSet();
        } catch (SQLException exception) {
            throw new ExecuteException("Exception while executing statement", exception);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            throw new ExecuteException("Exception while closing connection", exception);
        }
    }
}
